package com.wow.delivery.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendEvent(String topic, Object dto) {
        try {
            String json = objectMapper.writeValueAsString(dto);
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
}
