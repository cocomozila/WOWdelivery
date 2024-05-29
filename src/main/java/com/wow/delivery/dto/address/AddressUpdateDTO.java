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
    private String addressName;

    @NotNull
    private String detailedAddress;

    @NotNull
    private Double locationX;

    @NotNull
    private Double locationY;

    public AddressUpdateDTO() {}

    @Builder
    public AddressUpdateDTO(Long addressId, String addressAlias, String addressName, String detailedAddress, Double locationX, Double locationY) {
        this.addressId = addressId;
        this.addressAlias = addressAlias;
        this.addressName = addressName;
        this.detailedAddress = detailedAddress;
        this.locationX = locationX;
        this.locationY = locationY;
    }
}
