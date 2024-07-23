package com.wow.delivery.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentCancelRequest {

    private String paymentKey;
    private String cancelReason;

    @JsonProperty("orderId")
    private String transactionId;

    @Builder
    public PaymentCancelRequest(String paymentKey, String cancelReason, String transactionId) {
        this.paymentKey = paymentKey;
        this.cancelReason = cancelReason;
        this.transactionId = transactionId;
    }
}
