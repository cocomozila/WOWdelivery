package com.wow.delivery.entity.order;

import com.wow.delivery.entity.BaseEntity;
import com.wow.delivery.entity.common.Address;
import com.wow.delivery.util.Base36UUIDGenerator;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders", uniqueConstraints = { @UniqueConstraint(name = "order_number_unique", columnNames = { "order_number" }) })
public class OrderEntity extends BaseEntity {

    @Comment(value = "유저 ID")
    private Long userId;

    @Comment(value = "가게 ID")
    private Long shopId;

    @Comment(value = "담당 라이더 ID")
    private Long riderId;

    @Comment(value = "거래 ID")
    private Long paymentId;

    @Comment(value = "배달 받을 주소")
    @Embedded
    private Address destination;

    @Comment(value = "주문 번호")
    @Column(name = "order_number", columnDefinition = "VARCHAR(10)", nullable = false)
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

    @Comment(value = "쿠폰 ID")
    @Column(name = "coupon_id")
    private Long couponId;

    @Builder
    public OrderEntity(Address destination, Long userId, Long shopId, Long paymentId, String orderRequest, Long orderPrice, Long deliveryFee, Long totalPaymentAmount, Long couponId) {
        this.destination = destination;
        this.userId = userId;
        this.shopId = shopId;
        this.paymentId = paymentId;
        this.orderNumber = Base36UUIDGenerator.generate(7);
        this.orderRequest = orderRequest;
        this.orderPrice = orderPrice;
        this.deliveryFee = deliveryFee;
        this.totalPaymentAmount = totalPaymentAmount;
        this.orderStatus = OrderStatus.CONFIRMING;
        this.couponId = couponId;
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

    public void assignRider(Long riderId) {
        this.riderId = riderId;
    }

    public boolean isSameRider(Long riderId) {
        return this.riderId.equals(riderId);
    }

    public boolean isDelivering() {
        return this.orderStatus == OrderStatus.DELIVERING;
    }
}
