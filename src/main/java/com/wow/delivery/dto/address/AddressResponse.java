package com.wow.delivery.dto.address;

import com.wow.delivery.entity.common.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddressResponse {

    private Long id;

    private String addressAlias; // 주소 별명

    private Address address;

    @Builder
    public AddressResponse(Long id, String addressAlias, Address address) {
        this.id = id;
        this.addressAlias = addressAlias;
        this.address = address;
    }
}
