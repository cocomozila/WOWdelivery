package com.wow.delivery.repository;

import com.wow.delivery.entity.menu.MenuCategoryEntity;

import java.util.List;
import java.util.Optional;

public interface MenuCategoryRepository extends CustomJpaRepository<MenuCategoryEntity, Long> {

    Optional<List<MenuCategoryEntity>> findAllByShopId(Long shopId);

    List<MenuCategoryEntity> findByIdIn(List<Long> menucategoryIds);
}

