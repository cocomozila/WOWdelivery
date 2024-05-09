package com.wow.delivery.error.exception;

import com.wow.delivery.error.ErrorCode;

public class InvalidParameterException extends CustomException {

    public InvalidParameterException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
