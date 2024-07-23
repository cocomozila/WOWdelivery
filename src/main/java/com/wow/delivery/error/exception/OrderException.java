package com.wow.delivery.error.exception;

import com.wow.delivery.error.ErrorCode;
import lombok.Getter;

@Getter
public class OrderException extends CustomException {

    public OrderException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
