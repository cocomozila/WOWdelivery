package com.wow.delivery.dto.shop;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NameNearbyShopRequestDTO extends NearbyShopRequestDTO {

    private String searchShopName; // 가게 상호 명

    @Builder(builderMethodName = "nameNearbyShopRequestDTOBuilder")
    public NameNearbyShopRequestDTO(Long userId, String searchShopName, String state, String city, String district, String streetName, String buildingNumber, String addressDetail, Double latitude, Double longitude) {
        super(userId, state, city, district, streetName, buildingNumber, addressDetail, latitude, longitude);
        this.searchShopName = searchShopName;
    }
}
