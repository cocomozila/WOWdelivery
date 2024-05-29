package com.wow.delivery.repository;

import com.wow.delivery.entity.Address;

import java.util.List;

public interface AddressRepository extends CustomJpaRepository<Address, Long> {

    List<Address> findAllByUserId(Long userId);
}
