package com.wow.delivery.dto.owner;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OwnerSigninResponse {

    private Long id; // 아이디

    @Builder
    public OwnerSigninResponse(Long id) {
        this.id = id;
    }
}
