package com.wow.delivery.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public enum ErrorCode {

    // DuplicateException
    DUPLICATE_EMAIL(BAD_REQUEST, "중복", "중복된 이메일입니다."),
    DUPLICATE_PHONE_NUMBER(BAD_REQUEST, "중복", "중복된 휴대폰 번호입니다."),

    // MismatchException
    MISMATCH_ACCOUNT(BAD_REQUEST, "불일치", "이메일이나 비밀번호가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
