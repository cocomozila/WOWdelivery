package com.wow.delivery.entity.shop;

import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class S2LevelToken {

    @Column(name = "s2_level_11_token", columnDefinition = "VARCHAR(20)", nullable = false)
    private String s2Level11Token;

    @Column(name = "s2_level_12_token", columnDefinition = "VARCHAR(20)", nullable = false)
    private String s2Level12Token;

    @Column(name = "s2_level_13_token", columnDefinition = "VARCHAR(20)", nullable = false)
    private String s2Level13Token;

    @Column(name = "s2_level_14_token", columnDefinition = "VARCHAR(20)", nullable = false)
    private String s2Level14Token;

    public S2LevelToken(Double latitude, Double longitude) {
        this.s2Level11Token = getS2CellIdToken(latitude, longitude, 11);
        this.s2Level12Token = getS2CellIdToken(latitude, longitude, 12);
        this.s2Level13Token = getS2CellIdToken(latitude, longitude, 13);
        this.s2Level14Token = getS2CellIdToken(latitude, longitude, 14);
    }

    private String getS2CellIdToken(Double latitude, Double longitude, int cellLevel) {
        S2LatLng latLng = S2LatLng.fromDegrees(latitude, longitude);
        return S2CellId.fromLatLng(latLng)
            .parent(cellLevel)
            .toToken();
    }
}
