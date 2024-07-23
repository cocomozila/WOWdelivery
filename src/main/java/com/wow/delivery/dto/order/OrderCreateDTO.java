package com.wow.delivery.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateDTO {

    @NotNull
    @Comment(value = "유저 ID")
    private Long userId;

    @NotNull
    @Comment(value = "가게 ID")
    private Long shopId;

    @NotBlank
    @Comment(value = "거래 UUID")
    @JsonProperty("orderId")
    private String transactionId;

    @NotNull
    private List<OrderCart> orderCart;

    @Comment(value = "주문 요청 사항")
    private String orderRequest;

    @Comment(value = "주문 금액")
    private Long orderPrice;

    @Comment(value = "배달비")
    private Long deliveryFee;

    @Comment(value = "총 결제 금액")
    private Long totalPaymentAmount;

    @Builder
    public OrderCreateDTO(Long userId, Long shopId, String transactionId, List<OrderCart> orderCart, String orderRequest, Long orderPrice, Long deliveryFee, Long totalPaymentAmount) {
        this.userId = userId;
        this.shopId = shopId;
        this.transactionId = transactionId;
        this.orderCart = orderCart;
        this.orderRequest = orderRequest;
        this.orderPrice = orderPrice;
        this.deliveryFee = deliveryFee;
        this.totalPaymentAmount = totalPaymentAmount;
    }
}
