package com.wow.delivery.error.exception;

import com.wow.delivery.error.ErrorCode;
import lombok.Getter;

@Getter
public class PaymentException extends CustomException {

    public PaymentException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
