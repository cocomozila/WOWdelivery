package com.wow.delivery.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSigninResponse {

    private Long id;

    @Builder
    public UserSigninResponse(Long id) {
        this.id = id;
    }
}
