package com.wow.delivery.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NearbyOrderRequestDTO {

    private String riderId; // 라이더 ID
    private String streetName; // 주소(도로명)
    private Double latitude; // 위도
    private Double longitude; // 경도

    @Builder
    public NearbyOrderRequestDTO(String riderId, String streetName, Double latitude, Double longitude) {
        this.riderId = riderId;
        this.streetName = streetName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
