package com.wow.delivery.dto.shop;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NearByShopResponse {

    private Long shopId;

    private String shopName; // 가게 명

    private int minOrderPrice; // 최소 주문 금액

    @Builder
    public NearByShopResponse(Long shopId, String shopName, int minOrderPrice) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.minOrderPrice = minOrderPrice;
    }
}
