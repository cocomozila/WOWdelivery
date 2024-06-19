package com.wow.delivery.repository;

import com.wow.delivery.entity.DeliveryAddress;

import java.util.List;

public interface AddressRepository extends CustomJpaRepository<DeliveryAddress, Long> {

    List<DeliveryAddress> findAllByUserId(Long userId);
}
