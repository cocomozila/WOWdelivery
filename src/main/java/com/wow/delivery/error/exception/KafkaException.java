package com.wow.delivery.error.exception;

import com.wow.delivery.error.ErrorCode;

public class KafkaException extends CustomException {

    public KafkaException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
