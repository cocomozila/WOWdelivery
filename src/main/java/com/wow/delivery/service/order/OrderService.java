package com.wow.delivery.service.order;

import com.wow.delivery.dto.order.*;
import com.wow.delivery.dto.order.details.OrderDetailsResponse;
import com.wow.delivery.entity.RiderEntity;
import com.wow.delivery.entity.UserEntity;
import com.wow.delivery.entity.order.OrderDetailsEntity;
import com.wow.delivery.entity.order.OrderEntity;
import com.wow.delivery.entity.order.OrderStatus;
import com.wow.delivery.entity.payment.PaymentEntity;
import com.wow.delivery.entity.shop.ShopEntity;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.error.exception.InvalidParameterException;
import com.wow.delivery.error.exception.OrderException;
import com.wow.delivery.error.exception.PaymentException;
import com.wow.delivery.kafka.KafkaTopics;
import com.wow.delivery.kafka.producer.OrderProducer;
import com.wow.delivery.repository.*;
import com.wow.delivery.service.S2Service;
import com.wow.delivery.service.shop.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderProducer orderProducer;
    private final S2Service s2Service;
    private final AppliedRiderRepository appliedRiderRepository;
    private final RiderRepository riderRepository;
    private final ShopService shopService;

    @Transactional
    @Retryable(retryFor = InvalidParameterException.class, backoff = @Backoff(delay = 300))
    public void createOrder(OrderCreateDTO createDTO) {
        UserEntity userEntity = userRepository.findByIdOrThrow(createDTO.getUserId(), ErrorCode.USER_DATA_NOT_FOUND, null);
        ShopEntity shopEntity = shopService.findByShopIdOrThrow(createDTO.getShopId());
        PaymentEntity paymentEntity = paymentRepository.findByTransactionIdOrThrow(createDTO.getTransactionId());
        validPayment(paymentEntity, createDTO);

        OrderEntity orderEntity = OrderEntity.builder()
            .userId(userEntity.getIdOrThrow())
            .shopId(shopEntity.getIdOrThrow())
            .paymentId(paymentEntity.getIdOrThrow())
            .destination(createDTO.getAddress())
            .orderRequest(createDTO.getOrderRequest())
            .orderPrice(createDTO.getOrderPrice())
            .deliveryFee(createDTO.getDeliveryFee())
            .totalPaymentAmount(createDTO.getTotalPaymentAmount())
            .couponId(createDTO.getCouponId())
            .build();

        validOrderNumber(orderEntity.getOrderNumber());

        OrderEntity saveOrderEntity = orderRepository.save(orderEntity);

        List<OrderDetailsEntity> orderDetailEntities = createDTO.getOrderCart()
            .stream()
            .map(c -> OrderDetailsEntity.builder()
                .orderId(saveOrderEntity.getIdOrThrow())
                .menuId(c.getMenuId())
                .amount(c.getAmount())
                .build())
            .toList();

        orderDetailsRepository.saveAll(orderDetailEntities);

        List<OrderDetailsResponse> orderDetailsResponses = createDTO.getOrderCart()
            .stream()
            .map(o -> OrderDetailsResponse.builder()
                .menuId(o.getMenuId())
                .amount(o.getAmount())
                .build())
            .toList();

        OrderResponse orderResponse = OrderResponse.builder()
            .shopId(saveOrderEntity.getShopId())
            .orderNumber(saveOrderEntity.getOrderNumber())
            .orderStatus(saveOrderEntity.getOrderStatus())
            .orderRequest(saveOrderEntity.getOrderRequest())
            .orderPrice(saveOrderEntity.getOrderPrice())
            .address(saveOrderEntity.getDestination())
            .deliveryFee(saveOrderEntity.getDeliveryFee())
            .totalPaymentAmount(saveOrderEntity.getTotalPaymentAmount())
            .orderDetailsResponses(orderDetailsResponses)
            .couponId(saveOrderEntity.getCouponId())
            .build();

        orderProducer.sendEvent(KafkaTopics.CREATE_ORDER, orderResponse);
    }

    private void validPayment(PaymentEntity paymentEntity, OrderCreateDTO createDTO) {
        if (!paymentEntity.getAmount().equals(createDTO.getTotalPaymentAmount())) {
            throw new PaymentException(ErrorCode.INVALID_PARAMETER, "주문 총 금액이 결제 금액과 일치하지 않습니다.");
        }
    }

    private void validOrderNumber(String orderNumber) {
        Optional<OrderEntity> order = orderRepository.findByOrderNumber(orderNumber);
        if (order.isPresent()) {
            throw new InvalidParameterException(ErrorCode.INVALID_PARAMETER, "중복된 주문번호 입니다.");
        }
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findByIdOrThrow(orderId, ErrorCode.ORDER_DATA_NOT_FOUND, null);
        List<OrderDetailsResponse> orderDetailsResponses = orderDetailsRepository.findAllByOrderIdOrThrow(orderId)
            .stream()
            .map(o -> OrderDetailsResponse.builder()
                .menuId(o.getMenuId())
                .amount(o.getAmount())
                .build())
            .toList();

        return OrderResponse.builder()
            .shopId(orderEntity.getShopId())
            .orderNumber(orderEntity.getOrderNumber())
            .orderStatus(orderEntity.getOrderStatus())
            .orderRequest(orderEntity.getOrderRequest())
            .orderPrice(orderEntity.getOrderPrice())
            .deliveryFee(orderEntity.getDeliveryFee())
            .totalPaymentAmount(orderEntity.getTotalPaymentAmount())
            .orderDetailsResponses(orderDetailsResponses)
            .build();
    }

    @Transactional
    public void cancelOrder(OrderCancelDTO cancelDTO) {
        OrderEntity orderEntity = orderRepository.findByIdOrThrow(cancelDTO.getOrderId(), ErrorCode.ORDER_DATA_NOT_FOUND, null);
        if (!orderEntity.isCancelAbleStatus()) {
            throw new OrderException(ErrorCode.STATUS_CHANGE_NOT_ALLOWED, "확인 중 상태만 주문취소가 가능합니다.");
        }
        orderEntity.orderCancel();
    }

    @Transactional
    public void cancelOrder(OrderResponse orderResponse) {
        OrderEntity order = orderRepository.findByOrderNumber(orderResponse.getOrderNumber())
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.ORDER_DATA_NOT_FOUND, "주문 데이터를 찾을 수 없습니다."));
        order.orderCancel();
    }

    @Transactional
    public void acceptOrder(OrderAcceptDTO orderAcceptDTO) {
        OrderEntity order = orderRepository.findByIdOrThrow(orderAcceptDTO.getOrderId(), ErrorCode.ORDER_DATA_NOT_FOUND, null);
        order.updateOrderStatus(OrderStatus.PREPARING);
        orderProducer.sendEvent(KafkaTopics.ACCEPT_ORDER, orderAcceptDTO);
    }

    @Transactional
    public void rejectOrder(OrderAcceptDTO orderAcceptDTO) {
        OrderEntity order = orderRepository.findByIdOrThrow(orderAcceptDTO.getOrderId(), ErrorCode.ORDER_DATA_NOT_FOUND, null);
        order.updateOrderStatus(OrderStatus.CANCELED_OWNER);
        orderProducer.sendEvent(KafkaTopics.REJECT_ORDER, orderAcceptDTO);
    }

    @Transactional(readOnly = true)
    public List<OrderDeliveryResponse> getNearbyOrder(NearbyOrderRequestDTO requestDTO) {
        if (s2Service.isPopulatedStreetName(requestDTO.getStreetName())) {
            List<String> tokens = s2Service.getNearbyCellIdTokens(requestDTO.getLatitude(), requestDTO.getLongitude(), 2000, 13);
            return orderRepository.findNearbyOrderLevel13(tokens);
        }
        List<String> tokens = s2Service.getNearbyCellIdTokens(requestDTO.getLatitude(), requestDTO.getLongitude(), 4000, 12);
        return orderRepository.findNearbyOrderLevel12(tokens);
    }

    @Transactional
    public void pickupOrder(OrderDeliveryDTO orderDeliveryDTO) {
        OrderEntity order = orderRepository.findByIdOrThrow(orderDeliveryDTO.getOrderId(), ErrorCode.ORDER_DATA_NOT_FOUND, null);
        RiderEntity rider = riderRepository.findByIdOrThrow(orderDeliveryDTO.getRiderId(), ErrorCode.RIDER_DATA_NOT_FOUND, null);
        Long apply = appliedRiderRepository.addRider(order.getIdOrThrow());
        if (apply != 1) {
            throw new OrderException(ErrorCode.DUPLICATE_DATA, "이미 배차가 완료된 주문입니다.");
        }
        order.updateOrderStatus(OrderStatus.DELIVERING);
        order.assignRider(rider.getIdOrThrow());
        orderProducer.sendEvent(KafkaTopics.PICKUP_ORDER, orderDeliveryDTO);
    }

    @Transactional
    public void deliveredOrder(OrderDeliveryDTO orderDeliveryDTO) {
        OrderEntity order = orderRepository.findByIdOrThrow(orderDeliveryDTO.getOrderId(), ErrorCode.ORDER_DATA_NOT_FOUND, null);
        if (order.isSameRider(orderDeliveryDTO.getRiderId()) && order.isDelivering()) {
            order.updateOrderStatus(OrderStatus.DELIVERED);
            orderProducer.sendEvent(KafkaTopics.DELIVERED_ORDER, orderDeliveryDTO);
            appliedRiderRepository.removeRider(order.getIdOrThrow());
            return;
        }
        throw new OrderException(ErrorCode.STATUS_CHANGE_NOT_ALLOWED, "배달 중인 주문만 배달완료 할 수 있습니다.");
    }



    // todo
    // 1. 결제 프로세스를 마친 유저가 주문을 생성
    // 2. 메세지 큐(카프카)에 주문 정보를 넣는다.
    // 3. 메시지 큐에 담긴 주문 정보를 받아서 처리하는 것은 가게에 주문이 들어온 것을 알리는 "주문 알림 서비스"에서 처리한다.
    // 4. 주문 알림 서비스를 통해 가게 사장에게 주문 알림이 도착한다.
    // 5. 가게 사장은 주문을 승낙할지 거부할지 선택한다.
    //    5-1 (승낙) 가게 사장은 주문 상태를 PREPARING(준비중) 으로 변경한다.
    //       X -> 조리시간을 기입할 수 있다. (입력한 조리시간과 계산된 배달시간을 참고해서 유저에게 "배달예상시간"을 안내한다)
    //       O -> 배달 방법을 선택할 수 있다. 1.배달 대행사, 2.직접 배달, 3.잠시후 선택 (그러나 우리는 자동으로 1.배달 대행사만 구현한다)
    //       X -> 픽업 요청 시간을 선택할 수 있다. (10분 후, 15분 후 등등) 단, 고객에게 전달된 "배달예상시간"은 변경되지 않는다.
    //         -> 배차완료가 된 후에는 주문이 취소되어도 배달 취소가 불가능하다.
    //    5-2 (거부) 가게 사장은 주문 상태를 CANCELED(취소) 으로 변경한다.
    //    5-3  가게 사장이 주문상태를 변경하기 전 유저는 주문을 취소할 수 있다.
    // 6. (5-1 이후) 유저에게 카카오 알림톡을 보낸다 [주문이 완료되었습니다. 주문 번호는 0000입니다. 결제 금액은 0000원 입니다
    // .]
    // 7. 가게사장의 주문 승낙 이후, 라이더의 "배달 운행" 시작 시, 가게의 배달 요청 아이콘이 지도에 표시된다.
    // 8. 라이더는 가게의 배달 요청 아이콘을 클릭하여 접수가 가능하고, 주문에 라이더 ID를 업데이트 해준다.
    // 이벤트리스너만으로 활용해도 좋다.

    // 배달 프로세스
    // 1. 라이더가 근처 지역의 '준비중' 상태의 주문을 조회한다.
    // 2. '준비중' 상태의 근처 가게의 주문목록이 나온다.
    // 3. 라이더가 근처 '준비중' 상태의 주문목록을 하나 선택하여 배차받는다.
    // 4. 배차받았다고 가게 사장에게 알람이 간다.
    // 5. 라이더가 배차를 받고 주문의 상태를 '배달중'으로 변경한다.
    // 6. 라이더가 주문을 완료하고 '배달완료' 상태로 변경한다.
}
