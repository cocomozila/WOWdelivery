package com.wow.delivery.entity.coupon;

import lombok.Getter;

@Getter
public enum CouponStatus {
    UNUSED, // 미사용
    USED,   // 사용
    EXPIRED // 만료됨.
}
