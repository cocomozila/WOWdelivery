package com.wow.delivery.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossPaymentsRequest {

    private String orderId;
    private Long amount;

    @Builder
    public TossPaymentsRequest(String orderId, Long amount) {
        this.orderId = orderId;
        this.amount = amount;
    }
}
