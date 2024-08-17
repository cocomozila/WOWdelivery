package com.wow.delivery.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wow.delivery.dto.order.OrderResponse;
import com.wow.delivery.entity.coupon.CouponErrorEntity;
import com.wow.delivery.repository.CouponErrorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final CouponErrorRepository couponErrorRepository;

    @Retryable(retryFor = {KafkaException.class}, backoff = @Backoff(delay = 300))
    public void sendCouponId(String topic, OrderResponse orderResponse) {
        try {
            String json = objectMapper.writeValueAsString(orderResponse);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, json);

            // 비동기 작업 완료 시, 결과 또는 예외를 처리
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    throw new KafkaException(ex); // 예외를 다시 던져 재시도를 트리거
                }
            });

        } catch (JsonProcessingException e) {
            throw new KafkaException(e);
        }
    }

    @Recover
    public void recover(Exception e, String topic, OrderResponse orderResponse) {
        log.error("coupon error {} : {}", topic, orderResponse);
        couponErrorRepository.save(CouponErrorEntity.builder()
            .couponId(orderResponse.getCouponId())
            .usedTopic(topic)
            .build());
    }
}
