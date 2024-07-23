package com.wow.delivery.repository;

import com.wow.delivery.entity.shop.MetaCategoryEntity;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MetaCategoryRepository extends CustomJpaRepository<MetaCategoryEntity, Long> {

    List<MetaCategoryEntity> findAllByCategoryNameIn(@Param("categoryNames") List<String> categoryNames);
}
