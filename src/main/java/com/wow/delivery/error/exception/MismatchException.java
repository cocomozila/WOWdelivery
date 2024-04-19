package com.wow.delivery.error.exception;

import com.wow.delivery.error.ErrorCode;
import lombok.Getter;

import java.security.InvalidParameterException;

@Getter
public class MismatchException extends InvalidParameterException {

    private final ErrorCode errorCode;

    public MismatchException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
