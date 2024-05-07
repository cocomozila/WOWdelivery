package com.wow.delivery.error.exception;

import com.wow.delivery.error.ErrorCode;
import lombok.Getter;

@Getter
public class DataNotFoundException extends CustomException {

    public DataNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
