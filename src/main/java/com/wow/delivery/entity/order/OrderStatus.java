package com.wow.delivery.entity.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CONFIRMING,        // 확인중
    PREPARING,         // 준비중
    DELIVERING,        // 배달중
    DELIVERED,         // 배달완료
    CANCELED_USER,     // 유저의 주문 취소
    CANCELED_OWNER     // 가게 사장의 주문 취소
}
