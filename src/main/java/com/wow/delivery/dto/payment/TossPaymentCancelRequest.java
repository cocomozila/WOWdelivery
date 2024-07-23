package com.wow.delivery.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossPaymentCancelRequest {

    private String cancelReason;

    @Builder
    public TossPaymentCancelRequest(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
