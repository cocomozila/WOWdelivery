package com.wow.delivery.dto.address;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AddressResponse {

    private Long id;

    private String addressAlias; // 주소 별명

    private String addressName; // 주소명

    private String detailedAddress; // 상세주소

    private Double latitude; // 위도

    private Double longitude; // 경도

    @Builder
    public AddressResponse(Long id, String addressAlias, String addressName, String detailedAddress, Double latitude, Double longitude) {
        this.id = id;
        this.addressAlias = addressAlias;
        this.addressName = addressName;
        this.detailedAddress = detailedAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
