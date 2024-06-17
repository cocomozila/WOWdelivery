package com.wow.delivery.repository;

import com.wow.delivery.entity.shop.MetaCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MetaCategoryRepository extends CustomJpaRepository<MetaCategory, Long> {

    @Query(value = """
        SELECT m FROM MetaCategory m
         WHERE m.categoryName in :categoryNames
    """)
    List<MetaCategory> findAllByCategoryName(@Param("categoryNames") List<String> categoryNames);
}
