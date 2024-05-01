package com.wow.delivery.error.exception;

import com.wow.delivery.error.ErrorCode;

public class InvalidParameterException extends CustomException {

    public InvalidParameterException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static boolean isInstanceOf(RuntimeException e) {
        return e instanceof InvalidParameterException;
    }
}
