package com.wow.delivery.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Address extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "address_alias", columnDefinition = "VARCHAR(20)")
    private String addressAlias; // 주소 별명

    @Column(name = "state", columnDefinition = "VARCHAR(20)", nullable = false)
    private String state; // 주소(도)

    @Column(name = "city", columnDefinition = "VARCHAR(20)", nullable = false)
    private String city; // 주소(시)

    @Column(name = "district", columnDefinition = "VARCHAR(20)", nullable = false)
    private String district; // 주소(구,군)

    @Column(name = "street_name", columnDefinition = "VARCHAR(20)", nullable = false)
    private String streetName; // 주소(도로명)

    @Column(name = "building_number", columnDefinition = "VARCHAR(10)", nullable = false)
    private String buildingNumber; // 주소(빌딩 번호)

    @Column(name = "address_detail", columnDefinition = "VARCHAR(50)")
    private String addressDetail; // 상세주소

    @Column(name = "latitude", columnDefinition = "DOUBLE", nullable = false)
    private Double latitude; // 위도

    @Column(name = "longitude", columnDefinition = "DOUBLE", nullable = false)
    private Double longitude; // 경도

    @Builder
    public Address(User user, String addressAlias, String state, String city, String district, String streetName, String buildingNumber, String addressDetail, Double latitude, Double longitude) {
        this.user = user;
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

    public void update(String addressAlias, String state, String city, String district, String streetName, String buildingNumber, String addressDetail, Double latitude, Double longitude) {
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
