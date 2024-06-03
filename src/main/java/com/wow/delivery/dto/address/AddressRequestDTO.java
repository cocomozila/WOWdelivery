package com.wow.delivery.dto.address;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AddressRequestDTO {

    private Long userId;

    @Builder
    public AddressRequestDTO(Long userId) {
        this.userId = userId;
    }
}
