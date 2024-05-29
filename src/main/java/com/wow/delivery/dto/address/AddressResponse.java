package com.wow.delivery.dto.address;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AddressResponse {

    private Long id;

    private String addressAlias; // 주소 별명

    private String addressName; // 주소명

    private String detailedAddress; // 상세주소

    private Double locationX; // x좌표

    private Double locationY; // y좌표

    @Builder

    public AddressResponse(Long id, String addressAlias, String addressName, String detailedAddress, Double locationX, Double locationY) {
        this.id = id;
        this.addressAlias = addressAlias;
        this.addressName = addressName;
        this.detailedAddress = detailedAddress;
        this.locationX = locationX;
        this.locationY = locationY;
    }
}
