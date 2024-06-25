package com.wow.delivery.dto.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuRequest {

    private Long shopId;

    @Builder
    public MenuRequest(Long shopId) {
        this.shopId = shopId;
    }
}
