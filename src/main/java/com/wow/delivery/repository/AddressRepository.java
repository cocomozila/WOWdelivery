package com.wow.delivery.repository;

import com.wow.delivery.entity.DeliveryAddressEntity;

import java.util.List;

public interface AddressRepository extends CustomJpaRepository<DeliveryAddressEntity, Long> {

    List<DeliveryAddressEntity> findAllByUserId(Long userId);
}
