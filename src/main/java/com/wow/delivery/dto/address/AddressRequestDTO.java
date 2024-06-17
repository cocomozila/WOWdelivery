package com.wow.delivery.dto.address;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddressRequestDTO {

    private Long userId;

    @Builder
    public AddressRequestDTO(Long userId) {
        this.userId = userId;
    }
}
