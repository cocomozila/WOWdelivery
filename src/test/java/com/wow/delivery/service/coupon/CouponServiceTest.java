package com.wow.delivery.service.coupon;

import com.wow.delivery.dto.order.OrderResponse;
import com.wow.delivery.entity.coupon.CouponEntity;
import com.wow.delivery.entity.coupon.CouponStatus;
import com.wow.delivery.entity.order.OrderStatus;
import com.wow.delivery.kafka.KafkaTopics;
import com.wow.delivery.kafka.producer.CouponProducer;
import com.wow.delivery.repository.CouponRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Spy
    private CouponRepository couponRepository;

    @Mock
    private CouponProducer couponProducer;

    @Nested
    class UseCoupon {

        @Test
        @DisplayName("정상적인 쿠폰 사용")
        void success_useCoupon() {
            // given
            CouponEntity coupon = CouponEntity.builder()
                .orderId(1L)
                .couponName("5000원 할인쿠폰")
                .couponStatus(CouponStatus.UNUSED)
                .discountPrice(5000)
                .expireDate(Instant.ofEpochMilli(Instant.now().toEpochMilli() + 100000L))
                .build();

            coupon.setId(1L);

            given(couponRepository.findById(any()))
                .willReturn(Optional.of(coupon));

            OrderResponse orderResponse = OrderResponse.builder()
                .shopId(1L)
                .orderNumber("Test")
                .orderStatus(OrderStatus.CONFIRMING)
                .orderRequest("일회용품 NO")
                .orderPrice(18000L)
                .deliveryFee(3000L)
                .totalPaymentAmount(21000L)
                .couponId(1L)
                .build();

            // when
            couponService.useCoupon(orderResponse);

            // then
            Assertions.assertThat(coupon.getCouponStatus()).isEqualTo(CouponStatus.USED);
            then(couponProducer)
                .should(times(1))
                .sendCouponId(KafkaTopics.SUCCESS_COUPON, orderResponse);
        }

        @Test
        @DisplayName("기간 만료된 쿠폰 사용")
        void fail_useCoupon() {
            // given
            CouponEntity coupon = CouponEntity.builder()
                .orderId(1L)
                .couponName("5000원 할인쿠폰")
                .couponStatus(CouponStatus.UNUSED)
                .discountPrice(5000)
                .expireDate(Instant.ofEpochMilli(Instant.now().toEpochMilli() - 100000L))
                .build();

            coupon.setId(1L);

            given(couponRepository.findById(any()))
                .willReturn(Optional.of(coupon));

            OrderResponse orderResponse = OrderResponse.builder()
                .shopId(1L)
                .orderNumber("Test")
                .orderStatus(OrderStatus.CONFIRMING)
                .orderRequest("일회용품 NO")
                .orderPrice(18000L)
                .deliveryFee(3000L)
                .totalPaymentAmount(21000L)
                .couponId(1L)
                .build();

            // when
            couponService.useCoupon(orderResponse);

            // then
            Assertions.assertThat(coupon.getCouponStatus()).isEqualTo(CouponStatus.EXPIRED);
            then(couponProducer)
                .should(times(1))
                .sendCouponId(KafkaTopics.FAIL_COUPON, orderResponse);
        }
    }

}