package com.wow.delivery.service;

import com.wow.delivery.dto.order.OrderCancelDTO;
import com.wow.delivery.dto.order.OrderCart;
import com.wow.delivery.dto.order.OrderCreateDTO;
import com.wow.delivery.dto.order.OrderResponse;
import com.wow.delivery.entity.UserEntity;
import com.wow.delivery.entity.order.OrderDetailsEntity;
import com.wow.delivery.entity.order.OrderEntity;
import com.wow.delivery.entity.order.OrderStatus;
import com.wow.delivery.entity.payment.PaymentEntity;
import com.wow.delivery.entity.shop.ShopEntity;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.error.exception.OrderException;
import com.wow.delivery.error.exception.PaymentException;
import com.wow.delivery.repository.OrderDetailsRepository;
import com.wow.delivery.repository.OrderRepository;
import com.wow.delivery.repository.PaymentRepository;
import com.wow.delivery.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("주문")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    public OrderService orderService;

    @Spy
    private PaymentRepository paymentRepository;

    @Spy
    private OrderRepository orderRepository;

    @Spy
    private UserRepository userRepository;

    @Mock
    private ShopService shopService;

    @Spy
    private OrderDetailsRepository orderDetailsRepository;

    @Nested
    @DisplayName("생성")
    class create {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            UserEntity user = UserEntity.builder()
                .build();
            user.setId(1L);

            ShopEntity shop = ShopEntity.builder()
                .build();
            shop.setId(1L);

            PaymentEntity payment = PaymentEntity.builder()
                .amount(18000L)
                .build();
            payment.setId(1L);

            OrderCart orderCart = OrderCart.builder()
                .menuId(1L)
                .menuName("양념치킨")
                .price(15000L)
                .amount(1)
                .build();

            OrderCreateDTO createDTO = OrderCreateDTO.builder()
                .userId(1L)
                .shopId(1L)
                .transactionId(payment.getTransactionId())
                .orderCart(List.of(orderCart))
                .orderRequest("안전운전")
                .orderPrice(15000L)
                .deliveryFee(3000L)
                .totalPaymentAmount(18000L)
                .build();

            OrderEntity order = OrderEntity.builder()
                .build();
            order.setId(1L);

            given(userRepository.findById(any()))
                .willReturn(Optional.of(user));
            given(shopService.findByShopIdOrThrow(any()))
                .willReturn(shop);
            given(paymentRepository.findByTransactionId(any()))
                .willReturn(Optional.of(payment));
            given(orderRepository.save(any()))
                .willReturn(order);

            // when
            orderService.createOrder(createDTO);

            // then
            then(orderRepository)
                .should(times(1))
                .save(any());
            then(orderDetailsRepository)
                .should(times(1))
                .saveAll(any());
        }

        @Test
        @DisplayName("실패 - 주문 총 금액이 결제(Payments)와 불일치")
        void fail_payment_misMatch() {
            // given
            UserEntity user = UserEntity.builder()
                .build();
            user.setId(1L);

            ShopEntity shop = ShopEntity.builder()
                .build();
            shop.setId(1L);

            PaymentEntity payment = PaymentEntity.builder()
                .amount(22000L)
                .build();
            payment.setId(1L);

            OrderCart orderCart = OrderCart.builder()
                .menuId(1L)
                .menuName("양념치킨")
                .price(15000L)
                .amount(1)
                .build();

            OrderCreateDTO createDTO = OrderCreateDTO.builder()
                .userId(1L)
                .shopId(1L)
                .transactionId(payment.getTransactionId())
                .orderCart(List.of(orderCart))
                .orderRequest("안전운전")
                .orderPrice(15000L)
                .deliveryFee(3000L)
                .totalPaymentAmount(18000L)
                .build();

            OrderEntity order = OrderEntity.builder()
                .build();
            order.setId(1L);

            given(userRepository.findById(any()))
                .willReturn(Optional.of(user));
            given(shopService.findByShopIdOrThrow(any()))
                .willReturn(shop);
            given(paymentRepository.findByTransactionId(any()))
                .willReturn(Optional.of(payment));

            // when & then
            Assertions.assertThrows(PaymentException.class,
                () -> orderService.createOrder(createDTO));
        }
    }

    @Nested
    @DisplayName("조회")
    class get {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            OrderEntity order = OrderEntity.builder()
                .userId(1L)
                .shopId(1L)
                .paymentId(1L)
                .orderRequest("안전운전")
                .orderPrice(15000L)
                .deliveryFee(3000L)
                .totalPaymentAmount(18000L)
                .build();
            order.setId(1L);

            OrderDetailsEntity orderDetails = OrderDetailsEntity.builder()
                .orderId(1L)
                .menuId(1L)
                .amount(1)
                .build();

            given(orderRepository.findById(any()))
                .willReturn(Optional.of(order));
            given(orderDetailsRepository.findAllByOrderId(any()))
                .willReturn(Optional.of(List.of(orderDetails)));

            // when
            OrderResponse orderResponse = orderService.getOrder(1L);

            // then
            assertThat(orderResponse.getOrderDetailsResponses().size()).isEqualTo(1);
            assertThat(orderResponse.getOrderRequest()).isEqualTo("안전운전");
            assertThat(orderResponse.getOrderPrice()).isEqualTo(15000L);
            assertThat(orderResponse.getDeliveryFee()).isEqualTo(3000L);
            assertThat(orderResponse.getTotalPaymentAmount()).isEqualTo(18000L);
        }

        @Test
        @DisplayName("실패 - 상세주문을 찾을 수 없음")
        void fail_orderDetails_not_found() {
            // given
            OrderEntity order = OrderEntity.builder()
                .userId(1L)
                .shopId(1L)
                .paymentId(1L)
                .orderRequest("안전운전")
                .orderPrice(15000L)
                .deliveryFee(3000L)
                .totalPaymentAmount(18000L)
                .build();
            order.setId(1L);

            OrderDetailsEntity orderDetails = OrderDetailsEntity.builder()
                .orderId(1L)
                .menuId(1L)
                .amount(1)
                .build();

            given(orderRepository.findById(any()))
                .willReturn(Optional.of(order));
            given(orderDetailsRepository.findAllByOrderId(any()))
                .willReturn(Optional.empty());

            // when & then
            Assertions.assertThrows(DataNotFoundException.class,
                () -> orderService.getOrder(1L));
        }
    }

    @Nested
    @DisplayName("취소")
    class Cancel {

        @Test
        @DisplayName("성공 - '확인 중' 상태만 주문 취소가 가능")
        void success() {
            // given
            OrderEntity order = OrderEntity.builder()
                .userId(1L)
                .shopId(1L)
                .paymentId(1L)
                .orderRequest("안전운전")
                .orderPrice(15000L)
                .deliveryFee(3000L)
                .totalPaymentAmount(18000L)
                .build();
            order.setId(1L);

            OrderDetailsEntity orderDetails = OrderDetailsEntity.builder()
                .orderId(1L)
                .menuId(1L)
                .amount(1)
                .build();
            orderDetails.setId(1L);

            OrderCancelDTO cancelDTO = OrderCancelDTO.builder()
                .orderId(1L)
                .build();

            given(orderRepository.findById(any()))
                .willReturn(Optional.of(order));

            // when
            orderService.cancelOrder(cancelDTO);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED_USER);
        }

        @Test
        @DisplayName("실패 - 주문상태가 '준비중'")
        void fail_orderStatus_is_PREPARING() {
            // given
            OrderEntity order = OrderEntity.builder()
                .userId(1L)
                .shopId(1L)
                .paymentId(1L)
                .orderRequest("안전운전")
                .orderPrice(15000L)
                .deliveryFee(3000L)
                .totalPaymentAmount(18000L)
                .build();
            order.setId(1L);

            OrderDetailsEntity orderDetails = OrderDetailsEntity.builder()
                .orderId(1L)
                .menuId(1L)
                .amount(1)
                .build();
            orderDetails.setId(1L);

            OrderCancelDTO cancelDTO = OrderCancelDTO.builder()
                .orderId(1L)
                .build();

            order.updateOrderStatus(OrderStatus.PREPARING);

            given(orderRepository.findById(any()))
                .willReturn(Optional.of(order));

            // when & then
            Assertions.assertThrows(OrderException.class,
                () -> orderService.cancelOrder(cancelDTO));
        }
    }
}