package com.wow.delivery.dto.owner;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OwnerSigninResponse {

    private Long id;

    @Builder
    public OwnerSigninResponse(Long id) {
        this.id = id;
    }
}
