package com.wow.delivery.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // DuplicateException
    DUPLICATE_DATA("중복된 데이터 입니다."),

    // DataNotFoundException
    DATA_NOT_FOUND("데이터를 찾을 수 없습니다."),
    USER_DATA_NOT_FOUND("유저 데이터를 찾을 수 없습니다."),
    ADDRESS_DATA_NOT_FOUND("주소 데이터를 찾을 수 없습니다."),
    OWNER_DATA_NOT_FOUND("가게사장 데이터를 찾을 수 없습니다."),
    SHOP_DATA_NOT_FOUND("가게 데이터를 찾을 수 없습니다."),
    MENU_DATA_NOT_FOUND("메뉴 데이터를 찾을 수 없습니다."),
    MENU_CATEGORY_NOT_FOUND("메뉴 카테고리를 찾을 수 없습니다."),
    PAYMENT_DATA_NOT_FOUND("결제 데이터를 찾을 수 없습니다."),
    ORDER_DATA_NOT_FOUND("주문 데이터를 찾을 수 없습니다."),
    ORDER_DETAILS_NOT_FOUND("상세 주문 데이터를 찾을 수 없습니다."),
    COUPON_DATA_NOT_FOUND("쿠폰 데이터를 찾을 수 없습니다."),

    // Parameter
    INVALID_PARAMETER("유효하지 않은 입력 값입니다."),

    // Authentication
    UNAUTHENTICATED("인증이 필요합니다."),

    // FileException
    FILE_ERROR("파일 오류입니다."),

    // STATUS
    STATUS_CHANGE_NOT_ALLOWED("상태 변경이 불가능합니다."),

    // EXPIRE
    EXPIRE_DATA("만료된 데이터 입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
