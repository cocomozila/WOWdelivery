package com.wow.delivery.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossPaymentsRequest {

    private String orderId; // 주문 ID
    private Long amount; // 금액

    @Builder
    public TossPaymentsRequest(String orderId, Long amount) {
        this.orderId = orderId;
        this.amount = amount;
    }
}
