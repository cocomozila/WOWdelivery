package com.wow.delivery.error.exception;

import com.wow.delivery.error.ErrorCode;
import lombok.Getter;

@Getter
public class DataNotFoundException extends CustomException {

    public DataNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static boolean isInstanceOf(RuntimeException e) {
        return e.getClass().getSimpleName().equals("DataNotFoundException");
    }
}
