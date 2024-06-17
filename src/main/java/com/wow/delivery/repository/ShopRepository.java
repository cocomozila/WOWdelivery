package com.wow.delivery.repository;

import com.wow.delivery.entity.shop.Shop;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopRepository extends CustomJpaRepository<Shop, Long> {

    @Query(value = """
        SELECT s FROM Shop s
          LEFT JOIN ShopCategory c ON s.id = c.shopId
          LEFT JOIN MetaCategory m ON c.metaCategoryId = m.id
         WHERE s.s2LevelToken.s2Level12Token IN :tokens
           AND m.categoryName = :categoryName
    """)
    List<Shop> findByCategoryNearbyShopLevel12(@Param("tokens") List<String> tokens,
                                               @Param("categoryName")String categoryName);

    @Query(value = """
        SELECT s FROM Shop s
          LEFT JOIN ShopCategory c ON s.id = c.shopId
          LEFT JOIN MetaCategory m ON c.metaCategoryId = m.id
         WHERE s.s2LevelToken.s2Level13Token IN :tokens
           AND m.categoryName = :categoryName
    """)
    List<Shop> findByCategoryNearbyShopLevel13(@Param("tokens") List<String> tokens,
                                               @Param("categoryName")String categoryName);

    @Query(value = """
        SELECT s FROM Shop s
         WHERE s.s2LevelToken.s2Level12Token IN :tokens
           AND s.shopName LIKE %:shopName%
    """)
    List<Shop> findByShopNameNearbyShopLevel12(@Param("tokens") List<String> tokens,
                                               @Param("shopName")String shopName);

    @Query(value = """
        SELECT s FROM Shop s
         WHERE s.s2LevelToken.s2Level13Token IN :tokens
           AND s.shopName LIKE %:shopName%
    """)
    List<Shop> findByShopNameNearbyShopLevel13(@Param("tokens") List<String> tokens,
                                               @Param("shopName")String shopName);
}
