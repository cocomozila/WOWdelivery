package com.wow.delivery.repository;

import com.wow.delivery.entity.menu.MenuCategory;

import java.util.List;
import java.util.Optional;

public interface MenuCategoryRepository extends CustomJpaRepository<MenuCategory, Long> {

    Optional<List<MenuCategory>> findAllByShopId(Long shopId);

    List<MenuCategory> findByIdIn(List<Long> menucategoryIds);
}

