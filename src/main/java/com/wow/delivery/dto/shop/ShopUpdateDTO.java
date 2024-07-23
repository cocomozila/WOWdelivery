package com.wow.delivery.dto.shop;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@Getter
@NoArgsConstructor
public class ShopUpdateDTO {

    private Long shopId;

    private List<String> categoryNames; // 카테고리

    private String shopName; // 가게 명

    private String introduction; // 가게 소개

    private List<DayOfWeek> openDays; // 영업 요일

    private String openTime; // 영업시작 시간

    private String closeTime; // 영업종료 시간

    @Min(0)
    private int minOrderPrice; // 최소 주문 금액

    @Min(0)
    private int deliveryFee; // 배달비

    private String state; // 주소(도)

    private String city; // 주소(시)

    private String district; // 주소(구,군)

    private String streetName; // 주소(도로명)

    private String buildingNumber; // 주소(빌딩 번호)

    private String addressDetail; // 상세주소

    private Double latitude; // x좌표

    private Double longitude; // y좌표

    @Builder
    public ShopUpdateDTO(Long shopId, List<String> categoryNames, String shopName, String introduction, List<DayOfWeek> openDays, String openTime, String closeTime, int minOrderPrice, int deliveryFee, String state, String city, String district, String streetName, String buildingNumber, String addressDetail, Double latitude, Double longitude) {
        this.shopId = shopId;
        this.categoryNames = categoryNames;
        this.shopName = shopName;
        this.introduction = introduction;
        this.openDays = openDays;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minOrderPrice = minOrderPrice;
        this.deliveryFee = deliveryFee;
        this.state = state;
        this.city = city;
        this.district = district;
        this.streetName = streetName;
        this.buildingNumber = buildingNumber;
        this.addressDetail = addressDetail;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
