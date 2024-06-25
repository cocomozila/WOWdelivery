package com.wow.delivery.error.exception;

import com.wow.delivery.error.ErrorCode;

public class FileException extends CustomException {

    public FileException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
