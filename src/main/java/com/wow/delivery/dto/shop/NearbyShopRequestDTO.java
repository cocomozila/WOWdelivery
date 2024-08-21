package com.wow.delivery.dto.shop;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NearbyShopRequestDTO {

    private Long userId; // 유저 ID
    private String state; // 주소(도)
    private String city; // 주소(시)
    private String district; // 주소(구,군)
    private String streetName; // 주소(도로명)
    private String buildingNumber; // 주소(빌딩 번호)
    private String addressDetail; // 상세주소
    private Double latitude; // 위도
    private Double longitude; // 경도

    @Builder
    public NearbyShopRequestDTO(Long userId, String state, String city, String district, String streetName, String buildingNumber, String addressDetail, Double latitude, Double longitude) {
        this.userId = userId;
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
