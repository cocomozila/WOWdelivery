package com.wow.delivery.dto.rider;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RiderSigninResponse {

    private Long id;

    @Builder
    public RiderSigninResponse(Long id) {
        this.id = id;
    }
}
