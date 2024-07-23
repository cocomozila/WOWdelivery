package com.wow.delivery.repository;

import com.wow.delivery.entity.menu.MenuEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends CustomJpaRepository<MenuEntity, Long> {

    @Query(value = """
        SELECT m FROM MenuEntity m
         WHERE m.shopId = :shopId
         ORDER BY m.menuOrder ASC
    """)
    List<MenuEntity> findAllByShopIdOrderByOrder(@Param("shopId") Long shopId);

    List<MenuEntity> findByIdIn(List<Long> menuIds);
}
