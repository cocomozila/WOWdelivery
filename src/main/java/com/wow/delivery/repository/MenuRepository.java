package com.wow.delivery.repository;

import com.wow.delivery.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends CustomJpaRepository<Menu, Long> {

    List<Menu> findAllByIdOrderByMenuOrderAsc(Long id);
}
