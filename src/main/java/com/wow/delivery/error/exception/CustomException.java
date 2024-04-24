package com.wow.delivery.error.exception;

import com.wow.delivery.error.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
