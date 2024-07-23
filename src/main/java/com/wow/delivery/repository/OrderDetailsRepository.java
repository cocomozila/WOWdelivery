package com.wow.delivery.repository;

import com.wow.delivery.entity.order.OrderDetailsEntity;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;

import java.util.List;
import java.util.Optional;

public interface OrderDetailsRepository extends CustomJpaRepository<OrderDetailsEntity, Long> {
    Optional<List<OrderDetailsEntity>> findAllByOrderId(Long orderId);

    default List<OrderDetailsEntity> findAllByOrderIdOrThrow(Long orderId) {
        return findAllByOrderId(orderId).orElseThrow(() -> {
           throw new DataNotFoundException(ErrorCode.ORDER_DETAILS_NOT_FOUND, "상세 주문을 찾을 수 없습니다.");
        });
    }
}
