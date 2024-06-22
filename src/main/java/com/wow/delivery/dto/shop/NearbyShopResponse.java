package com.wow.delivery.dto.shop;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NearbyShopResponse {

    private Long shopId;

    private String shopName; // 가게 명

    private int minOrderPrice; // 최소 주문 금액

    @Builder
    public NearbyShopResponse(Long shopId, String shopName, int minOrderPrice) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.minOrderPrice = minOrderPrice;
    }
}
