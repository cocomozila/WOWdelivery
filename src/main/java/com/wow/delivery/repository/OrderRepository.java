package com.wow.delivery.repository;

import com.wow.delivery.entity.order.OrderEntity;

public interface OrderRepository extends CustomJpaRepository<OrderEntity, Long> {
}
