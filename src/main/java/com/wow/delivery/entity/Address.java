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

    @Column(name = "address_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String addressName; // 주소명

    @Column(name = "detailed_address", columnDefinition = "VARCHAR(30)")
    private String detailedAddress; // 상세주소

    @Column(name = "location_x", columnDefinition = "DOUBLE", nullable = false)
    private Double locationX; // x좌표

    @Column(name = "location_y", columnDefinition = "DOUBLE", nullable = false)
    private Double locationY; // y좌표

    @Builder
    public Address(User user, String addressAlias, String addressName, String detailedAddress, Double locationX, Double locationY) {
        this.user = user;
        this.addressAlias = addressAlias;
        this.addressName = addressName;
        this.detailedAddress = detailedAddress;
        this.locationX = locationX;
        this.locationY = locationY;
    }

    public void update(String addressAlias, String addressName, String detailedAddress, Double locationX, Double locationY) {
        this.addressAlias = addressAlias;
        this.addressName = addressName;
        this.detailedAddress = detailedAddress;
        this.locationX = locationX;
        this.locationY = locationY;
    }
}
