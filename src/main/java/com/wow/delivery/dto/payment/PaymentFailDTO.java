package com.wow.delivery.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentFailDTO {

    private String errorCode;
    private String errorMessage;

    @Builder
    public PaymentFailDTO(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
