package com.wow.delivery.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossPaymentCancelRequest {

    private String cancelReason; // 취소 사유

    @Builder
    public TossPaymentCancelRequest(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
