package com.wow.delivery.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderAcceptDTO {

    @NotNull
    private Long orderId; // 주문 ID

    @NotNull
    private Long userId; // 유저 ID

    @Builder
    public OrderAcceptDTO(Long orderId, Long userId) {
        this.orderId = orderId;
        this.userId = userId;
    }
}
