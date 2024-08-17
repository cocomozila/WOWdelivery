package com.wow.delivery.entity.coupon;

import com.wow.delivery.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "coupon_errors")
public class CouponErrorEntity extends BaseEntity {

    @Comment(value = "쿠폰 ID")
    private Long couponId;

    @Comment(value = "사용된 Topic")
    private String usedTopic;

    @Builder
    public CouponErrorEntity(Long couponId, String usedTopic) {
        this.couponId = couponId;
        this.usedTopic = usedTopic;
    }
}
