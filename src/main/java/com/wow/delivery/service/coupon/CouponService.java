package com.wow.delivery.service.coupon;

import com.wow.delivery.dto.order.OrderResponse;
import com.wow.delivery.entity.coupon.CouponEntity;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.CouponException;
import com.wow.delivery.error.exception.CustomException;
import com.wow.delivery.kafka.KafkaTopics;
import com.wow.delivery.kafka.producer.CouponProducer;
import com.wow.delivery.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponProducer couponProducer;
    private final CouponRepository couponRepository;

    @Transactional
    public void useCoupon(OrderResponse orderResponse) {
        try {
            CouponEntity coupon = couponRepository.findByIdOrThrow(orderResponse.getCouponId(), ErrorCode.COUPON_DATA_NOT_FOUND, null);
            if (coupon.isExpired()) {
                coupon.expireCoupon();
                throw new CouponException(ErrorCode.EXPIRE_DATA, "만료일이 지난 쿠폰입니다.");
            }
            coupon.useCoupon();
            couponProducer.sendCouponId(KafkaTopics.SUCCESS_COUPON, orderResponse);
        } catch (CustomException e) {
            couponProducer.sendCouponId(KafkaTopics.FAIL_COUPON, orderResponse);
        }
    }
}
