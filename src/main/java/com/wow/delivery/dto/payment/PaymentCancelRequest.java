package com.wow.delivery.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentCancelRequest {

    private String paymentKey; // 거래 Key
    private String cancelReason; // 취소 사유

    @JsonProperty("orderId")
    private String transactionId; // 주문 고유 UUID

    @Builder
    public PaymentCancelRequest(String paymentKey, String cancelReason, String transactionId) {
        this.paymentKey = paymentKey;
        this.cancelReason = cancelReason;
        this.transactionId = transactionId;
    }
}
