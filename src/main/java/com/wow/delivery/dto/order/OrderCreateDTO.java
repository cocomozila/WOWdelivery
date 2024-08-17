package com.wow.delivery.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wow.delivery.entity.common.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateDTO {

    @NotNull
    private Long userId; // 유저 ID

    @NotNull
    private Long shopId; // 가게 ID

    @NotBlank
    @JsonProperty("orderId")
    private String transactionId; // 거래 UUID

    @NotNull
    private List<OrderCart> orderCart;

    private String orderRequest; // 주문 요청 사항

    private Long orderPrice; // 주문 금액

    private Long deliveryFee; // 배달비

    private Long totalPaymentAmount; // 총 결제 금액

    private Address address; // 배달 받을 주소

    private Long couponId; // 쿠폰 ID

    @Builder
    public OrderCreateDTO(Long userId, Long shopId, String transactionId, List<OrderCart> orderCart, String orderRequest, Long orderPrice, Long deliveryFee, Long totalPaymentAmount, Address address, Long couponId) {
        this.userId = userId;
        this.shopId = shopId;
        this.transactionId = transactionId;
        this.orderCart = orderCart;
        this.orderRequest = orderRequest;
        this.orderPrice = orderPrice;
        this.deliveryFee = deliveryFee;
        this.totalPaymentAmount = totalPaymentAmount;
        this.address = address;
        this.couponId = couponId;
    }
}
