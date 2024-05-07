package com.wow.delivery.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // DuplicateException
    DUPLICATE_DATA("중복된 데이터 입니다."),

    // DataNotFoundException
    DATA_NOT_FOUND("데이터를 찾을 수 없습니다."),

    // Parameter
    INVALID_PARAMETER("유효하지 않은 입력 값입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
