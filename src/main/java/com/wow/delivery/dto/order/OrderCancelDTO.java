package com.wow.delivery.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCancelDTO {

    private Long orderId;

    @Builder
    public OrderCancelDTO(Long orderId) {
        this.orderId = orderId;
    }
}
