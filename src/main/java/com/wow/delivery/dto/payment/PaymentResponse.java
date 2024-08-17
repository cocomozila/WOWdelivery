package com.wow.delivery.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentResponse {

    private Long userId; // 유저 ID

    private String payType; // 지불 방법

    private String transactionId; // 주문 고유 UUID

    private Long amount; // 지불 금액

    private String successUrl; // 결제 성공 시 콜백 주소

    private String failUrl; // 결제 실패 시 콜백 주소

    @Builder
    public PaymentResponse(Long userId, String payType, Long amount, String transactionId, String successUrl, String failUrl) {
        this.userId = userId;
        this.payType = payType;
        this.amount = amount;
        this.transactionId = transactionId;
        this.successUrl = successUrl;
        this.failUrl = failUrl;
    }
}
