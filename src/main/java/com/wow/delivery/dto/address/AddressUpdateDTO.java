package com.wow.delivery.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AddressUpdateDTO {

    @NotNull
    private Long addressId;

    @NotBlank
    private String addressAlias;

    @NotBlank
    private String state; // 주소(도)

    @NotBlank
    private String city; // 주소(시)

    @NotBlank
    private String district; // 주소(구,군)

    @NotBlank
    private String streetName; // 주소(도로명)

    @NotBlank
    private String buildingNumber; // 주소(빌딩 번호)

    private String addressDetail; // 상세주소

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    public AddressUpdateDTO() {}

    @Builder
    public AddressUpdateDTO(Long addressId, String addressAlias, String state, String city, String district, String streetName, String buildingNumber, String addressDetail, Double latitude, Double longitude) {
        this.addressId = addressId;
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
