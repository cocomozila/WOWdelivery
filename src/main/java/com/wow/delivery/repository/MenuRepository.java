package com.wow.delivery.repository;

import com.wow.delivery.entity.menu.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends CustomJpaRepository<Menu, Long> {

    @Query(value = """
        SELECT m FROM Menu m
         WHERE m.shopId = :shopId
         ORDER BY m.menuOrder ASC
    """)
    List<Menu> findAllByShopIdOrderByOrder(@Param("shopId") Long shopId);

    List<Menu> findByIdIn(List<Long> menuIds);
}
