package com.wow.delivery.dto.order.details;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
public class OrderDetailsResponse {

    @Comment(value = "메뉴 ID")
    private Long menuId;

    @Comment(value = "수량")
    private int amount;

    @Builder
    public OrderDetailsResponse(Long menuId, int amount) {
        this.menuId = menuId;
        this.amount = amount;
    }
}
