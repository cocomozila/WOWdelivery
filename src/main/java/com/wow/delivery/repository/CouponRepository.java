package com.wow.delivery.repository;

import com.wow.delivery.entity.coupon.CouponEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends CustomJpaRepository<CouponEntity, Long> {
}
