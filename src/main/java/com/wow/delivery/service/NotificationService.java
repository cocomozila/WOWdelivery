package com.wow.delivery.service;

import com.wow.delivery.dto.order.OrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void sendAppNotification(OrderResponse orderResponse) {
        // 비동기로 작성
        // 앱으로 알림 전송
        log.info("해당 매장으로 주문생성 알림 전송 : {}", orderResponse.getShopId());
    }

    public void sendKakaoAcceptOrderNotification(Long shopId) {
        // 비동기로 작성
        // 앱으로 알림 전송
        log.info("해당 유저에게 주문 성공 카카오 알림 전송 : {}", shopId);
    }

    public void sendKakaoRejectOrderNotification(Long shopId) {
        // 비동기로 작성
        // 앱으로 알림 전송
        log.info("해당 유저에게 주문 실패 카카오 알림 전송 : {} ", shopId);
    }

    public void sendKakaoOrderPickupNotification(Long orderId) {
        // 비동기로 작성
        // 앱으로 알림 전송
        log.info("배달 픽업 카카오 알림 전송 : {} ", orderId);
    }

    public void sendKakaoOrderDeliveredNotification(Long orderId) {
        // 비동기로 작성
        // 앱으로 알림 전송
        log.info("배달 완료 카카오 알림 전송 : {} ", orderId);
    }
}
