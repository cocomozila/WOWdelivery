package com.wow.delivery.entity.payment;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING("결제전"),         // 결제 전 상태
    COMPLETED("결제완료"),      // 결제 완료 상태
    CANCELLED("결제취소"),      // 결제 취소 상태
    FAILED("결제실패"),         // 결제 실패 상태
    REFUNDED("환불완료");       // 결제 환불 상태

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }
}
