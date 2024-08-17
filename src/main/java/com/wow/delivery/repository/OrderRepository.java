package com.wow.delivery.repository;

import com.wow.delivery.entity.order.OrderEntity;

import java.util.Optional;

public interface OrderRepository extends CustomJpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderNumber(String orderNumber);
}
