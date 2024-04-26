package com.wow.delivery.error.exception;

import com.wow.delivery.error.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    public CustomException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
