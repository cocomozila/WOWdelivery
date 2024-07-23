package com.wow.delivery.service;

import com.wow.delivery.dto.payment.PaymentCancelRequest;
import com.wow.delivery.dto.payment.PaymentFailDTO;
import com.wow.delivery.dto.payment.PaymentRequest;
import com.wow.delivery.dto.payment.PaymentResponse;
import com.wow.delivery.entity.UserEntity;
import com.wow.delivery.entity.payment.PaymentEntity;
import com.wow.delivery.entity.payment.PaymentStatus;
import com.wow.delivery.error.exception.PaymentException;
import com.wow.delivery.repository.PaymentRepository;
import com.wow.delivery.repository.UserRepository;
import com.wow.delivery.service.payment.PaymentService;
import com.wow.delivery.service.payment.TossPaymentApiSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("거래")
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Spy
    private PaymentRepository paymentRepository;

    @Spy
    private UserRepository userRepository;

    @Mock
    private TossPaymentApiSender tossPaymentApiSender;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(paymentService, "TEST_TOSS_SECRET_API_KEY", "test_sk_jExPeJWYVQbKnokMJYaxr49R5gvN:");
        ReflectionTestUtils.setField(paymentService, "TEST_TOSS_SUCCESS_CALLBACK_URL", "http://localhost:8080/api/payments/success");
        ReflectionTestUtils.setField(paymentService, "TEST_TOSS_FAIL_CALLBACK_URL", "http://localhost:8080/api/payments/fail");
        ReflectionTestUtils.setField(paymentService, "TOSS_ORIGIN_URL", "https://api.tosspayments.com/v1/payments/");
    }

    @Nested
    @DisplayName("요청")
    class create {

        @Test
        @DisplayName("성공 - DB 저장 후 PaymentResponse 정상 반환")
        void success() {
            // given
            UserEntity user = UserEntity.builder()
                .build();
            user.setId(1L);

            PaymentRequest request = PaymentRequest.builder()
                .userId(1L)
                .payType("카드")
                .amount(30000L)
                .build();

            PaymentEntity payment = PaymentEntity.builder()
                .userId(1L)
                .payType("카드")
                .amount(30000L)
                .build();
            payment.setId(1L);

            given(paymentRepository.save(any()))
                .willReturn(payment);
            given(userRepository.findById(any()))
                .willReturn(Optional.of(user));

            // when
            PaymentResponse paymentResponse = paymentService.requestPayment(request);

            // then
            assertThat(paymentResponse.getUserId()).isEqualTo(1L);
            assertThat(paymentResponse.getPayType()).isEqualTo("카드");
            assertThat(paymentResponse.getAmount()).isEqualTo(30000L);
            assertThat(paymentResponse.getTransactionId()).isNotNull();
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
        }
    }

    @Nested
    @DisplayName("요청승인 Success")
    class SuccessPayment {

        @Test
        @DisplayName("결제요청 검증 통과")
        void verifyRequest_success() {
            // given
            PaymentEntity payment = PaymentEntity.builder()
                .userId(1L)
                .payType("카드")
                .amount(30000L)
                .build();
            payment.setId(1L);

            String paymentKey = "test_paymentKey";
            String transactionId = payment.getTransactionId();
            Long amount = 30000L;


            given(paymentRepository.findByTransactionId(any()))
                .willReturn(Optional.of(payment));

            // when
            paymentService.verifyRequest(paymentKey, transactionId, amount);

            // then
            assertThat(payment.getPaymentKey()).isEqualTo("test_paymentKey");
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        }

        @Test
        @DisplayName("결제요청 에러 - 결제 금액 상이")
        void verifyRequest_fail() {
            // given
            PaymentEntity payment = PaymentEntity.builder()
                .userId(1L)
                .payType("카드")
                .amount(30000L)
                .build();
            payment.setId(1L);

            String paymentKey = "test_paymentKey";
            String transactionId = payment.getTransactionId();
            Long amount = 20000L;


            given(paymentRepository.findByTransactionId(any()))
                .willReturn(Optional.of(payment));

            // when & then
            assertThatThrownBy(() -> paymentService.verifyRequest(paymentKey, transactionId, amount))
                .isInstanceOf(PaymentException.class)
                .hasMessage("결제 금액이 일치하지 않습니다.");
            assertThat(payment.getPaymentKey()).isEqualTo("test_paymentKey");
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELLED);
        }
    }

    @Nested
    @DisplayName("요청승인 Fail")
    class FailPayment {

        @Test
        @DisplayName("정상적인 페이먼츠사 에러코드 반환")
        void requestFail() {
            // given
            PaymentEntity payment = PaymentEntity.builder()
                .userId(1L)
                .payType("카드")
                .amount(30000L)
                .build();
            payment.setId(1L);

            String errorCode = "NOT_FOUND_PAYMENT";
            String errorMessage = "존재하지 않는 결제 입니다.";
            String transactionId = payment.getTransactionId();


            given(paymentRepository.findByTransactionId(any()))
                .willReturn(Optional.of(payment));

            // when
            PaymentFailDTO paymentFailDTO = paymentService.requestFail(errorCode, errorMessage, transactionId);

            // then
            assertThat(paymentFailDTO.getErrorCode()).isEqualTo("NOT_FOUND_PAYMENT");
            assertThat(paymentFailDTO.getErrorMessage()).isEqualTo("존재하지 않는 결제 입니다.");
        }
    }

    @Nested
    @DisplayName("결제 취소 요청")
    class Cancel {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            PaymentEntity payment = PaymentEntity.builder()
                .userId(1L)
                .payType("카드")
                .amount(30000L)
                .build();
            payment.setId(1L);

            PaymentCancelRequest request = PaymentCancelRequest.builder()
                .paymentKey("test_paymentKey")
                .cancelReason("테스트 취소사유")
                .transactionId(payment.getTransactionId())
                .build();

            given(paymentRepository.findByTransactionId(any()))
                .willReturn(Optional.of(payment));
            given(tossPaymentApiSender.requestTossCancelPayment(any(), any(), any()))
                .willReturn("ok");

            // when
            paymentService.cancelPayment(request);

            // then
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.REFUNDED);
        }
    }
}