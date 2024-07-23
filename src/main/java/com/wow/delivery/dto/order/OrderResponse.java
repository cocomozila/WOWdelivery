package com.wow.delivery.dto.order;

import com.wow.delivery.dto.order.details.OrderDetailsResponse;
import com.wow.delivery.entity.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderResponse {

    @Comment(value = "주문 번호")
    private String orderNumber;

    @Comment(value = "주문 상태")
    private OrderStatus orderStatus;

    @Comment(value = "주문 요청사항")
    private String orderRequest;

    @Comment(value = "주문 금액")
    private Long orderPrice;

    @Comment(value = "배달비")
    private Long deliveryFee;

    @Comment(value = "총 결제금액")
    private Long totalPaymentAmount;

    @Comment(value = "메뉴들")
    private List<OrderDetailsResponse> orderDetailsResponses;

    @Builder
    public OrderResponse(String orderNumber, OrderStatus orderStatus, String orderRequest, Long orderPrice, Long deliveryFee, Long totalPaymentAmount, List<OrderDetailsResponse> orderDetailsResponses) {
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.orderRequest = orderRequest;
        this.orderPrice = orderPrice;
        this.deliveryFee = deliveryFee;
        this.totalPaymentAmount = totalPaymentAmount;
        this.orderDetailsResponses = orderDetailsResponses;
    }
}
