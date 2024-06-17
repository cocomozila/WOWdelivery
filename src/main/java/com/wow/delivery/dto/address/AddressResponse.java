package com.wow.delivery.dto.address;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AddressResponse {

    private Long id;

    private String addressAlias; // 주소 별명

    private String state; // 주소(도)

    private String city; // 주소(시)

    private String district; // 주소(구,군)

    private String streetName; // 주소(도로명)

    private String buildingNumber; // 주소(빌딩 번호)

    private String addressDetail; // 상세주소

    private Double latitude; // 위도

    private Double longitude; // 경도

    @Builder

    public AddressResponse(Long id, String addressAlias, String state, String city, String district, String streetName, String buildingNumber, String addressDetail, Double latitude, Double longitude) {
        this.id = id;
        this.addressAlias = addressAlias;
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
