package com.wow.delivery.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Embeddable
@NoArgsConstructor
@Getter
public class Address {

    @Comment(value = "주소(도)")
    @Column(name = "state", columnDefinition = "VARCHAR(20)", nullable = false)
    private String state;

    @Comment(value = "주소(시)")
    @Column(name = "city", columnDefinition = "VARCHAR(20)", nullable = false)
    private String city;

    @Comment(value = "주소(구,군)")
    @Column(name = "district", columnDefinition = "VARCHAR(20)", nullable = false)
    private String district;

    @Comment(value = "주소(도로명)")
    @Column(name = "street_name", columnDefinition = "VARCHAR(20)", nullable = false)
    private String streetName;

    @Comment(value = "주소(빌딩 번호)")
    @Column(name = "building_number", columnDefinition = "VARCHAR(10)", nullable = false)
    private String buildingNumber;

    @Comment(value = "상세주소")
    @Column(name = "address_detail", columnDefinition = "VARCHAR(50)")
    private String addressDetail;

    @Comment(value = "위도")
    @Column(name = "latitude", columnDefinition = "DOUBLE", nullable = false)
    private Double latitude;

    @Comment(value = "경도")
    @Column(name = "longitude", columnDefinition = "DOUBLE", nullable = false)
    private Double longitude;

    @Builder
    public Address(String state, String city, String district, String streetName, String buildingNumber, String addressDetail, Double latitude, Double longitude) {
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
