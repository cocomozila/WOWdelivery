package com.wow.delivery.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AddressCreateDTO {

    @NotNull
    private Long userId;

    @NotBlank
    private String addressAlias;

    @NotBlank
    private String addressName;

    @NotNull
    private String detailedAddress;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    public AddressCreateDTO() {}

    @Builder
    public AddressCreateDTO(Long userId, String addressAlias, String addressName, String detailedAddress, Double latitude, Double longitude) {
        this.userId = userId;
        this.addressAlias = addressAlias;
        this.addressName = addressName;
        this.detailedAddress = detailedAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
