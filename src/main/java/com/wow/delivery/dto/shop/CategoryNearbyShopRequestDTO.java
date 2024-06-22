package com.wow.delivery.dto.shop;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryNearbyShopRequestDTO extends NearbyShopRequestDTO {

    private String searchCategory; // 카테고리

    @Builder(builderMethodName = "categoryNearbyShopRequestDTOBuilder")
    public CategoryNearbyShopRequestDTO(Long userId, String searchCategory, String state, String city, String district, String streetName, String buildingNumber, String addressDetail, Double latitude, Double longitude) {
        super(userId, state, city, district, streetName, buildingNumber, addressDetail, latitude, longitude);
        this.searchCategory = searchCategory;
    }
}
