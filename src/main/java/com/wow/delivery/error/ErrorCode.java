package com.wow.delivery.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // DuplicateException
    DUPLICATE_EMAIL("중복된 이메일입니다."),
    DUPLICATE_PHONE_NUMBER("중복된 휴대폰 번호입니다."),

    // DataNotFoundException
    MISMATCH_ACCOUNT("일치하는 계정을 찾을 수 없습니다."),

    // Parameter
    INVALID_PARAMETER("유효하지 않은 입력 값입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
