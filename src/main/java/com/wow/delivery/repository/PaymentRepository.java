package com.wow.delivery.repository;

import com.wow.delivery.entity.payment.PaymentEntity;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.PaymentException;

import java.util.Optional;

public interface PaymentRepository extends CustomJpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByTransactionId(String transactionId);

    default PaymentEntity findByTransactionIdOrThrow(String transactionId) {
        return findByTransactionId(transactionId)
            .orElseThrow(() -> new PaymentException(ErrorCode.PAYMENT_DATA_NOT_FOUND, "결제 데이터를 찾을 수 업습니다."));
    }
}
