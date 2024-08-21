package com.wow.delivery.dto.order;

import com.wow.delivery.entity.common.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDeliveryResponse {

    private Long orderId; // 주문 Id

    private Long shopId; // 가게 ID

    private String shopName; // 가게 명

    private Address shopAddress; // 가게 주소

    private Address destination; // 배달 수령지

    @Builder
    public OrderDeliveryResponse(Long orderId, Long shopId, String shopName, Address shopAddress, Address destination) {
        this.orderId = orderId;
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.destination = destination;
    }
}
