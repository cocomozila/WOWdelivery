package com.wow.delivery.dto.order.details;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDetailsResponse {

    private Long menuId; // 메뉴 ID

    private int amount; // 수량

    @Builder
    public OrderDetailsResponse(Long menuId, int amount) {
        this.menuId = menuId;
        this.amount = amount;
    }
}
