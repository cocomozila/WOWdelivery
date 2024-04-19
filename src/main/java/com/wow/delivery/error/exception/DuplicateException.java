package com.wow.delivery.error.exception;

import com.wow.delivery.error.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicateException extends RuntimeException {

    private final ErrorCode errorCode;

    public DuplicateException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
