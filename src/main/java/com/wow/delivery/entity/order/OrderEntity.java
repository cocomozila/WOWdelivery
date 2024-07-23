package com.wow.delivery.entity.order;

import com.wow.delivery.entity.BaseEntity;
import com.wow.delivery.util.SnowFlakeGenerator;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @Comment(value = "유저 ID")
    private Long userId;

    @Comment(value = "가게 ID")
    private Long shopId;

    @Comment(value = "라이더 ID")
    private Long riderId;

    @Comment(value = "거래 ID")
    private Long paymentId;

    @Comment(value = "주문 번호")
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Comment(value = "주문 상태")
    @Column(name = "order_status", columnDefinition = "VARCHAR(20)", nullable = false)
    private OrderStatus orderStatus;

    @Comment(value = "주문 요청사항")
    @Column(name = "order_request", columnDefinition = "VARCHAR(255)")
    private String orderRequest;

    @Comment(value = "주문 금액")
    @Column(name = "order_price", nullable = false)
    private Long orderPrice;

    @Comment(value = "배달비")
    @Column(name = "delivery_fee", nullable = false)
    private Long deliveryFee;

    @Comment(value = "총 결제금액")
    @Column(name = "total_price", nullable = false)
    private Long totalPaymentAmount;

    @Builder
    public OrderEntity(Long userId, Long shopId, Long paymentId, String orderRequest, Long orderPrice, Long deliveryFee, Long totalPaymentAmount) {
        this.userId = userId;
        this.shopId = shopId;
        this.paymentId = paymentId;
        this.orderNumber = SnowFlakeGenerator.generateBase36String(); // 16진수로 넣기
        this.orderRequest = orderRequest;
        this.orderPrice = orderPrice;
        this.deliveryFee = deliveryFee;
        this.totalPaymentAmount = totalPaymentAmount;
        this.orderStatus = OrderStatus.CONFIRMING;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void orderCancel() {
        this.orderStatus = OrderStatus.CANCELED_USER;
    }

    public boolean isCancelAbleStatus() {
        return this.getOrderStatus().equals(OrderStatus.CONFIRMING);
    }
}
