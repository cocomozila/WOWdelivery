package com.wow.delivery.entity.shop;

import com.wow.delivery.entity.BaseEntity;
import com.wow.delivery.entity.Owner;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
public class Shop extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Comment(value = "가게 명")
    @Column(name = "shop_name", columnDefinition = "VARCHAR(20)", nullable = false)
    private String shopName;

    @Comment(value = "가게 소개")
    @Column(name = "introduction", columnDefinition = "VARCHAR(255)")
    private String introduction;

    private BusinessHours businessHours;

    @Comment(value = "최소 주문 금액")
    @Column(name = "min_order_price", columnDefinition = "INT")
    private int minOrderPrice;

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

    @Embedded
    private S2LevelToken s2LevelToken;

    @Builder
    public Shop(Owner owner, String shopName, String introduction, BusinessHours businessHours, int minOrderPrice, String state, String city, String district, String streetName, String buildingNumber, String addressDetail, Double latitude, Double longitude, S2LevelToken s2LevelToken) {
        this.owner = owner;
        this.shopName = shopName;
        this.introduction = introduction;
        this.businessHours = businessHours;
        this.minOrderPrice = minOrderPrice;
        this.state = state;
        this.city = city;
        this.district = district;
        this.streetName = streetName;
        this.buildingNumber = buildingNumber;
        this.addressDetail = addressDetail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.s2LevelToken = s2LevelToken;
    }

    public void update(String shopName, String introduction, BusinessHours businessHours, int minOrderPrice, String state, String city, String district, String streetName, String buildingNumber, String addressDetail, Double latitude, Double longitude, S2LevelToken s2LevelToken) {
        this.shopName = shopName;
        this.introduction = introduction;
        this.businessHours = businessHours;
        this.minOrderPrice = minOrderPrice;
        this.state = state;
        this.city = city;
        this.district = district;
        this.streetName = streetName;
        this.buildingNumber = buildingNumber;
        this.addressDetail = addressDetail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.s2LevelToken = s2LevelToken;
    }
}
