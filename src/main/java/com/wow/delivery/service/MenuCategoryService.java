package com.wow.delivery.service;

import com.wow.delivery.dto.menu.category.MenuCategoryCreateDTO;
import com.wow.delivery.dto.menu.category.MenuCategoryOrderUpdateDTO;
import com.wow.delivery.dto.menu.category.MenuCategoryResponse;
import com.wow.delivery.dto.menu.category.MenuCategoryUpdateDTO;
import com.wow.delivery.entity.menu.MenuCategory;
import com.wow.delivery.entity.shop.Shop;
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
        Shop shop = shopService.findByShopIdOrThrow(createDTO.getShopId());
        MenuCategory menuCategory = MenuCategory.builder()
            .shopId(shop.getIdOrThrow())
            .name(createDTO.getName())
            .build();
        MenuCategory saveMenuCategory = menuCategoryRepository.save(menuCategory);
        saveMenuCategory.createCategoryOrder();
    }

    @Transactional(readOnly = true)
    public List<MenuCategoryResponse> getMenuCategory(Long shopId) {
        Shop shop = shopService.findByShopIdOrThrow(shopId);
        return menuCategoryRepository.findAllByShopId(shop.getIdOrThrow())
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
        MenuCategory menuCategory = menuCategoryRepository.findByIdOrThrow(updateDTO.getMenuCategoryId(), ErrorCode.MENU_CATEGORY_NOT_FOUND, null);
        menuCategory.update(updateDTO.getName());
    }

    @Transactional
    public void reorderMenuCategories(MenuCategoryOrderUpdateDTO updateDTO) {
        List<Long> beforeIds = updateDTO.getBeforeIds();
        List<Long> afterIds = updateDTO.getAfterIds();

        List<MenuCategory> menuCategories = menuCategoryRepository.findByIdIn(beforeIds);

        Map<Long, MenuCategory> menuCategoryMap = menuCategories.stream()
            .collect(Collectors.toMap(MenuCategory::getId, mc -> mc));

        List<Integer> sortedMenuCategoriesOrders = beforeIds.stream()
            .map(id -> menuCategoryMap.get(id).getMenuCategoryOrder())
            .toList();

        IntStream.range(0, updateDTO.getSize())
            .filter(i -> !beforeIds.get(i).equals(afterIds.get(i)))
            .forEach(i -> {
                MenuCategory menuCategory = menuCategoryMap.get(afterIds.get(i));
                menuCategory.setMenuCategoryOrder(sortedMenuCategoriesOrders.get(i));
            });
    }
}
