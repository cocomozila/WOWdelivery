package com.wow.delivery.repository;

import com.wow.delivery.dto.order.OrderDeliveryResponse;
import com.wow.delivery.entity.order.OrderEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CustomJpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    @Query("""
    SELECT new com.wow.delivery.dto.order.OrderDeliveryResponse(
           o.id,
           s.id,
           s.shopName,
           s.address,
           o.destination)
      FROM OrderEntity o
      LEFT JOIN ShopEntity s ON o.shopId = s.id
     WHERE s.s2LevelToken.s2Level12Token IN :tokens
       AND o.orderStatus = 'PREPARING'
    """)
    List<OrderDeliveryResponse> findNearbyOrderLevel12(@Param("tokens") List<String> tokens);

    @Query("""
    SELECT new com.wow.delivery.dto.order.OrderDeliveryResponse(
           o.id,
           s.id,
           s.shopName,
           s.address,
           o.destination)
      FROM OrderEntity o
      LEFT JOIN ShopEntity s ON o.shopId = s.id
     WHERE s.s2LevelToken.s2Level13Token IN :tokens
       AND o.orderStatus = 'PREPARING'
    """)
    List<OrderDeliveryResponse> findNearbyOrderLevel13(@Param("tokens") List<String> tokens);
}
