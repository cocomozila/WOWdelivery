package com.wow.delivery.dto.order;

import com.wow.delivery.dto.order.details.OrderDetailsResponse;
import com.wow.delivery.entity.common.Address;
import com.wow.delivery.entity.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderResponse {

    private Long shopId; // 가게 ID

    private String orderNumber; // 주문 번호

    private OrderStatus orderStatus; // 주문 상태

    private String orderRequest; // 주문 요청사항

    private Long orderPrice; // 주문 금액

    private Long deliveryFee; // 배달비

    private Long totalPaymentAmount; // 총 결제금액

    private Address address; // 배달 받을 주소

    private List<OrderDetailsResponse> orderDetailsResponses; // 메뉴들

    private Long couponId; // 쿠폰 ID

    @Builder
    public OrderResponse(Long shopId, String orderNumber, OrderStatus orderStatus, String orderRequest, Long orderPrice, Long deliveryFee, Long totalPaymentAmount, Address address, List<OrderDetailsResponse> orderDetailsResponses, Long couponId) {
        this.shopId = shopId;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.orderRequest = orderRequest;
        this.orderPrice = orderPrice;
        this.deliveryFee = deliveryFee;
        this.totalPaymentAmount = totalPaymentAmount;
        this.address = address;
        this.orderDetailsResponses = orderDetailsResponses;
        this.couponId = couponId;
    }
}
