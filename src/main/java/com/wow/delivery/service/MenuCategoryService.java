package com.wow.delivery.service;

import com.wow.delivery.dto.menu.category.MenuCategoryCreateDTO;
import com.wow.delivery.dto.menu.category.MenuCategoryOrderUpdateDTO;
import com.wow.delivery.dto.menu.category.MenuCategoryResponse;
import com.wow.delivery.dto.menu.category.MenuCategoryUpdateDTO;
import com.wow.delivery.entity.menu.MenuCategoryEntity;
import com.wow.delivery.entity.shop.ShopEntity;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.repository.MenuCategoryRepository;
import com.wow.delivery.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MenuCategoryService {

    private final MenuCategoryRepository menuCategoryRepository;
    private final ShopRepository shopRepository;
    private final ShopService shopService;

    @Transactional
    public void createMenuCategory(MenuCategoryCreateDTO createDTO) {
        ShopEntity shopEntity = shopService.findByShopIdOrThrow(createDTO.getShopId());
        MenuCategoryEntity menuCategoryEntity = MenuCategoryEntity.builder()
            .shopId(shopEntity.getIdOrThrow())
            .name(createDTO.getName())
            .build();
        MenuCategoryEntity saveMenuCategoryEntity = menuCategoryRepository.save(menuCategoryEntity);
        saveMenuCategoryEntity.createCategoryOrder();
    }

    @Transactional(readOnly = true)
    public List<MenuCategoryResponse> getMenuCategory(Long shopId) {
        ShopEntity shopEntity = shopService.findByShopIdOrThrow(shopId);
        return menuCategoryRepository.findAllByShopId(shopEntity.getIdOrThrow())
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.MENU_CATEGORY_NOT_FOUND, "메뉴 카테고리가 존재하지 않습니다."))
            .stream()
            .map(mc -> MenuCategoryResponse.builder()
                .MenuCategoryId(mc.getIdOrThrow())
                .name(mc.getName())
                .menuCategoryOrder(mc.getMenuCategoryOrder())
                .build())
            .toList();
    }

    @Transactional
    public void updateMenuCategory(MenuCategoryUpdateDTO updateDTO) {
        MenuCategoryEntity menuCategoryEntity = menuCategoryRepository.findByIdOrThrow(updateDTO.getMenuCategoryId(), ErrorCode.MENU_CATEGORY_NOT_FOUND, null);
        menuCategoryEntity.update(updateDTO.getName());
    }

    @Transactional
    public void reorderMenuCategories(MenuCategoryOrderUpdateDTO updateDTO) {
        List<Long> beforeIds = updateDTO.getBeforeIds();
        List<Long> afterIds = updateDTO.getAfterIds();

        List<MenuCategoryEntity> menuCategories = menuCategoryRepository.findByIdIn(beforeIds);

        Map<Long, MenuCategoryEntity> menuCategoryMap = menuCategories.stream()
            .collect(Collectors.toMap(MenuCategoryEntity::getId, mc -> mc));

        List<Integer> sortedMenuCategoriesOrders = beforeIds.stream()
            .map(id -> menuCategoryMap.get(id).getMenuCategoryOrder())
            .toList();

        IntStream.range(0, updateDTO.getSize())
            .filter(i -> !beforeIds.get(i).equals(afterIds.get(i)))
            .forEach(i -> {
                MenuCategoryEntity menuCategoryEntity = menuCategoryMap.get(afterIds.get(i));
                menuCategoryEntity.setMenuCategoryOrder(sortedMenuCategoriesOrders.get(i));
            });
    }
}
