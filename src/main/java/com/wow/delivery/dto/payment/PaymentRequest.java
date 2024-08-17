package com.wow.delivery.dto.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentRequest {

    @NotNull
    private Long userId; // 유저 ID

    @NotNull
    private String payType; // 지불 방법

    @Min(0)
    private Long amount; // 지불 금액

    @Builder
    public PaymentRequest(Long userId, String payType, Long amount) {
        this.userId = userId;
        this.payType = payType;
        this.amount = amount;
    }
}
