package com.wow.delivery.service.payment;

import com.wow.delivery.dto.payment.PaymentCancelRequest;
import com.wow.delivery.dto.payment.PaymentFailDTO;
import com.wow.delivery.dto.payment.PaymentRequest;
import com.wow.delivery.dto.payment.PaymentResponse;
import com.wow.delivery.entity.UserEntity;
import com.wow.delivery.entity.payment.PaymentEntity;
import com.wow.delivery.entity.payment.PaymentStatus;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.error.exception.PaymentException;
import com.wow.delivery.repository.PaymentRepository;
import com.wow.delivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final TossPaymentClient tossPaymentClient;

    @Value("${test.toss.success.callback.url}")
    private String TEST_TOSS_SUCCESS_CALLBACK_URL;

    @Value("${test.toss.fail.callback.url}")
    private String TEST_TOSS_FAIL_CALLBACK_URL;

    @Value("${test.toss.origin.url}")
    private String TOSS_ORIGIN_URL;

    @Transactional
    public PaymentResponse requestPayment(PaymentRequest request) {
        UserEntity userEntity = userRepository.findByIdOrThrow(request.getUserId(), ErrorCode.USER_DATA_NOT_FOUND, "유저를 찾을 수 없습니다.");
        PaymentEntity paymentEntity = PaymentEntity.builder()
            .userId(userEntity.getIdOrThrow())
            .payType(request.getPayType())
            .amount(request.getAmount())
            .build();
        PaymentEntity savePaymentEntity = paymentRepository.save(paymentEntity);

        return PaymentResponse.builder()
            .userId(savePaymentEntity.getUserId())
            .payType(savePaymentEntity.getPayType())
            .transactionId(savePaymentEntity.getTransactionId())
            .amount(savePaymentEntity.getAmount())
            .successUrl(TEST_TOSS_SUCCESS_CALLBACK_URL)
            .failUrl(TEST_TOSS_FAIL_CALLBACK_URL)
            .build();
    }

    @Transactional
    public void verifyRequest(String paymentKey, String transactionId, Long amount) {
        paymentRepository.findByTransactionId(transactionId)
            .ifPresentOrElse(paymentEntity -> {
                    if (paymentEntity.getAmount().equals(amount)) {
                        paymentEntity.setPaymentKey(paymentKey);
                        paymentEntity.updateStatus(PaymentStatus.COMPLETED);
                    } else {
                        paymentEntity.setPaymentKey(paymentKey);
                        paymentEntity.updateStatus(PaymentStatus.CANCELLED);
                        throw new PaymentException(ErrorCode.SHOP_DATA_NOT_FOUND, "결제 금액이 일치하지 않습니다.");
                    }
                }, () -> {
                    throw new DataNotFoundException(ErrorCode.PAYMENT_DATA_NOT_FOUND, "결제 데이터를 찾을 수 없습니다.");
                }
            );
    }

    public String requestFinalPayment(String paymentKey, String transactionId, Long amount) {
        String uri = TOSS_ORIGIN_URL + paymentKey;
        return tossPaymentClient.requestTossFinalPayment(uri, transactionId, amount);
    }

    @Transactional
    public PaymentFailDTO requestFail(String errorCode, String errorMessage, String transactionId) {
        paymentRepository.findByTransactionId(transactionId)
            .ifPresentOrElse(paymentEntity ->
                paymentEntity.updateStatus(PaymentStatus.FAILED),
                () -> {
                    throw new DataNotFoundException(ErrorCode.PAYMENT_DATA_NOT_FOUND, "결제 데이터를 찾을 수 없습니다.");
                }
            );
        return PaymentFailDTO.builder()
            .errorCode(errorCode)
            .errorMessage(errorMessage)
            .build();
    }

    public String cancelPayment(PaymentCancelRequest request) {
        String uri = TOSS_ORIGIN_URL + request.getPaymentKey() + "/cancel";
        String cancelResponse = tossPaymentClient.requestTossCancelPayment(uri, request.getCancelReason());

        if (cancelResponse == null) {
            throw new PaymentException(ErrorCode.INVALID_PARAMETER, "잘못된 API 요청 입니다.");
        }

        PaymentEntity paymentEntity = paymentRepository.findByTransactionIdOrThrow(request.getTransactionId());
        paymentEntity.updateStatus(PaymentStatus.REFUNDED);
        return cancelResponse;
    }
}
