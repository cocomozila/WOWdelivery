package com.wow.delivery.dto.shop;

import com.wow.delivery.entity.common.Address;
import com.wow.delivery.entity.shop.BusinessHours;
import com.wow.delivery.entity.shop.DayOfWeekListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ShopResponse {

    @Comment(value = "가게 명")
    private String shopName;

    @Comment(value = "가게 소개")
    private String introduction;

    @Comment(value = "영업 시간")
    private BusinessHours businessHours;

    @Comment(value = "주소")
    private Address address;

    @Column(name = "open_days")
    @Convert(converter = DayOfWeekListConverter.class)
    private List<DayOfWeek> openDays = new ArrayList<>();

    @Comment(value = "최소 주문 금액")
    private int minOrderPrice;

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
