package com.wow.delivery.repository;

import com.wow.delivery.entity.shop.ShopEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopRepository extends CustomJpaRepository<ShopEntity, Long> {

    @Query(value = """
        SELECT s FROM ShopEntity s
          LEFT JOIN ShopCategoryEntity c ON s.id = c.shopId
          LEFT JOIN MetaCategoryEntity m ON c.metaCategoryId = m.id
         WHERE s.s2LevelToken.s2Level12Token IN :tokens
           AND m.categoryName = :categoryName
    """)
    List<ShopEntity> findByCategoryNearbyShopLevel12(@Param("tokens") List<String> tokens,
                                                     @Param("categoryName")String categoryName);

    @Query(value = """
        SELECT s FROM ShopEntity s
          LEFT JOIN ShopCategoryEntity c ON s.id = c.shopId
          LEFT JOIN MetaCategoryEntity m ON c.metaCategoryId = m.id
         WHERE s.s2LevelToken.s2Level13Token IN :tokens
           AND m.categoryName = :categoryName
    """)
    List<ShopEntity> findByCategoryNearbyShopLevel13(@Param("tokens") List<String> tokens,
                                                     @Param("categoryName")String categoryName);

    @Query(value = """
        SELECT s FROM ShopEntity s
         WHERE s.s2LevelToken.s2Level12Token IN :tokens
           AND s.shopName LIKE %:shopName%
    """)
    List<ShopEntity> findByShopNameNearbyShopLevel12(@Param("tokens") List<String> tokens,
                                                     @Param("shopName")String shopName);

    @Query(value = """
        SELECT s FROM ShopEntity s
         WHERE s.s2LevelToken.s2Level13Token IN :tokens
           AND s.shopName LIKE %:shopName%
    """)
    List<ShopEntity> findByShopNameNearbyShopLevel13(@Param("tokens") List<String> tokens,
                                                     @Param("shopName")String shopName);
}
