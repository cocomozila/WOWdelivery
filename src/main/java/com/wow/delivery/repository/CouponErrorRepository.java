package com.wow.delivery.repository;

import com.wow.delivery.entity.coupon.CouponErrorEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponErrorRepository extends CustomJpaRepository<CouponErrorEntity, Long> {
}
