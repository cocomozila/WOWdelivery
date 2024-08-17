package com.wow.delivery.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wow.delivery.dto.order.OrderResponse;
import com.wow.delivery.kafka.KafkaTopics;
import com.wow.delivery.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @KafkaListener(topics = {KafkaTopics.FAIL_COUPON}, groupId = "kafkaTest")
    public void receiveMessage(ConsumerRecord<String, String> record) {
        try {
            OrderResponse orderResponse = objectMapper.readValue(record.value(), new TypeReference<>() {});
            orderService.cancelOrder(orderResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
