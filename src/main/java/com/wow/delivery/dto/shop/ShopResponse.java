package com.wow.delivery.dto.shop;

import com.wow.delivery.entity.common.Address;
import com.wow.delivery.entity.shop.BusinessHours;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ShopResponse {

    private final String shopName; // 가게명

    private final String introduction; // 가게 소개

    private final BusinessHours businessHours; // 영업 시간

    private final Address address; // 주소

    private List<DayOfWeek> openDays = new ArrayList<>(); // 영업하는 요일

    private final int minOrderPrice; // 최소 주문 금액

    @Builder
    public ShopResponse(String shopName, String introduction, BusinessHours businessHours, Address address, List<DayOfWeek> openDays, int minOrderPrice) {
        this.shopName = shopName;
        this.introduction = introduction;
        this.businessHours = businessHours;
        this.address = address;
        this.openDays = openDays;
        this.minOrderPrice = minOrderPrice;
    }
}
