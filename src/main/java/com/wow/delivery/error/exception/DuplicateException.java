package com.wow.delivery.error.exception;

import com.wow.delivery.error.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicateException extends CustomException {

    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
