package com.wow.delivery.dto.shop;

import com.wow.delivery.entity.common.Address;
import com.wow.delivery.entity.shop.BusinessHours;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ShopResponse {

    private String shopName; // 가게명

    private String introduction; // 가게 소개

    private BusinessHours businessHours; // 영업 시간

    private Address address; // 주소

    private List<DayOfWeek> openDays = new ArrayList<>(); // 영업하는 요일

    private int minOrderPrice; // 최소 주문 금액

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
