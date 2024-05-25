package com.wow.delivery.error.exception;

import com.wow.delivery.error.ErrorCode;
import lombok.Getter;

@Getter
public class AuthenticationException extends CustomException {

    public AuthenticationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
