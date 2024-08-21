package com.wow.delivery.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDeliveryDTO {

    @NotNull
    private Long orderId; // 주문 ID

    @NotNull
    private Long riderId; // 라이더 ID

    @Builder
    public OrderDeliveryDTO(Long orderId, Long riderId) {
        this.orderId = orderId;
        this.riderId = riderId;
    }
}
