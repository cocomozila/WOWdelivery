package com.wow.delivery.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wow.delivery.dto.order.OrderAcceptDTO;
import com.wow.delivery.kafka.KafkaTopics;
import com.wow.delivery.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserComsumer {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = {KafkaTopics.ACCEPT_ORDER}, groupId = "kafkaTest")
    public void receiveOrderAccept(ConsumerRecord<String, String> record) {
        try {
            OrderAcceptDTO orderAcceptDTO = objectMapper.readValue(record.value(), new TypeReference<>() {});
            notificationService.sendKakaoAcceptOrderNotification(orderAcceptDTO.getUserId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = {KafkaTopics.REJECT_ORDER}, groupId = "kafkaTest")
    public void receiveOrderReject(ConsumerRecord<String, String> record) {
        try {
            OrderAcceptDTO orderAcceptDTO = objectMapper.readValue(record.value(), new TypeReference<>() {});
            notificationService.sendKakaoRejectOrderNotification(orderAcceptDTO.getUserId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
