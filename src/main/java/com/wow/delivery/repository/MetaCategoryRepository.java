package com.wow.delivery.repository;

import com.wow.delivery.entity.shop.MetaCategory;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MetaCategoryRepository extends CustomJpaRepository<MetaCategory, Long> {

    List<MetaCategory> findAllByCategoryNameIn(@Param("categoryNames") List<String> categoryNames);
}
