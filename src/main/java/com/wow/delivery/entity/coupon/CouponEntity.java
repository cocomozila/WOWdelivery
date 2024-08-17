package com.wow.delivery.entity.coupon;

import com.wow.delivery.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "coupons")
public class CouponEntity extends BaseEntity {

    @Comment(value = "주문 ID")
    private Long orderId;

    @Column(name = "coupon_name", columnDefinition = "VARCHAR(30)", nullable = false)
    @Comment(value = "쿠폰 이름")
    private String couponName;

    @Column(name = "discount_price", columnDefinition = "INTEGER", nullable = false)
    @Comment(value = "할인 가격")
    private int discountPrice;

    @Enumerated(EnumType.STRING)
    @Comment(value = "쿠폰 상태")
    @Column(name = "coupon_status", columnDefinition = "VARCHAR(20)", nullable = false)
    private CouponStatus couponStatus;

    @Comment(value = "쿠폰 만료일")
    @Column(name = "expire_date", columnDefinition = "DATETIME(6)", nullable = false)
    private Instant expireDate;

    @Builder
    public CouponEntity(Long orderId, String couponName, int discountPrice, CouponStatus couponStatus, Instant expireDate) {
        this.orderId = orderId;
        this.couponName = couponName;
        this.discountPrice = discountPrice;
        this.couponStatus = couponStatus;
        this.expireDate = expireDate;
    }

    public void useCoupon() {
        this.couponStatus = CouponStatus.USED;
    }

    public void expireCoupon() {
        this.couponStatus = CouponStatus.EXPIRED;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expireDate.toEpochMilli();
    }
}
