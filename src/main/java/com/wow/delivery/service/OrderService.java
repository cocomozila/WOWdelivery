package com.wow.delivery.service;

import com.wow.delivery.dto.order.OrderCancelDTO;
import com.wow.delivery.dto.order.OrderCreateDTO;
import com.wow.delivery.dto.order.OrderResponse;
import com.wow.delivery.dto.order.details.OrderDetailsResponse;
import com.wow.delivery.entity.UserEntity;
import com.wow.delivery.entity.order.OrderDetailsEntity;
import com.wow.delivery.entity.order.OrderEntity;
import com.wow.delivery.entity.payment.PaymentEntity;
import com.wow.delivery.entity.shop.ShopEntity;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.OrderException;
import com.wow.delivery.error.exception.PaymentException;
import com.wow.delivery.repository.OrderDetailsRepository;
import com.wow.delivery.repository.OrderRepository;
import com.wow.delivery.repository.PaymentRepository;
import com.wow.delivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ShopService shopService;
    private final OrderDetailsRepository orderDetailsRepository;

    @Transactional
    public void createOrder(OrderCreateDTO createDTO) {
        UserEntity userEntity = userRepository.findByIdOrThrow(createDTO.getUserId(), ErrorCode.USER_DATA_NOT_FOUND, null);
        ShopEntity shopEntity = shopService.findByShopIdOrThrow(createDTO.getShopId());
        PaymentEntity paymentEntity = paymentRepository.findByTransactionIdOrThrow(createDTO.getTransactionId());
        validPayment(paymentEntity, createDTO);

        OrderEntity orderEntity = OrderEntity.builder()
            .userId(userEntity.getIdOrThrow())
            .shopId(shopEntity.getIdOrThrow())
            .paymentId(paymentEntity.getIdOrThrow())
            .orderRequest(createDTO.getOrderRequest())
            .orderPrice(createDTO.getOrderPrice())
            .deliveryFee(createDTO.getDeliveryFee())
            .totalPaymentAmount(createDTO.getTotalPaymentAmount())
            .build();

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
    }

    private void validPayment(PaymentEntity paymentEntity, OrderCreateDTO createDTO) {
        if (!paymentEntity.getAmount().equals(createDTO.getTotalPaymentAmount())) {
            throw new PaymentException(ErrorCode.INVALID_PARAMETER, "주문 총 금액이 결제 금액과 일치하지 않습니다.");
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

    // todo
    // 1. 결제 프로세스를 마친 유저가 주문을 생성
    // 2. 메세지 큐에 주문 정보를 넣는다.
    // 3. 메시지 큐에 담긴 주문 정보를 받아서 처리하는 것은 가게에 주문이 들어온 것을 알리는 "주문 알림 서비스"에서 처리한다.
    // 4. 주문 알림 서비스를 통해 가게 사장에게 주문 알림이 도착한다.
    // 5. 가게 사장은 주문을 승낙할지 거부할지 선택한다.
    //    5-1 (승낙) 가게 사장은 주문 상태를 PREPARING(준비중) 으로 변경한다.
    //    5-2 (거부) 가게 사장은 주문 상태를 CANCELED(취소) 으로 변경한다.
    //    5-3  가게 사장이 주문상태를 변경하기 전 유저는 주문을 취소할 수 있다.
    // 6. (5-1 이후) 유저에게 카카오 알림톡을 보낸다 [주문이 완료되었습니다. 주문 번호는 0000입니다. 결제 금액은 0000원 입니다.]
    // 7. 가게사장의 주문 승낙 이후, 라이더의 "배달 운행" 시작 시, 가게의 배달 요청 아이콘이 지도에 표시된다.
    // 8. 라이더는 가게의 배달 요청 아이콘을 클릭하여 접수가 가능하고, 주문에 라이더 ID를 업데이트 해준다.
    // 이벤트리스너만으로 활용해도 좋다.
}
