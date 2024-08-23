package com.wow.delivery.service;

import com.wow.delivery.dto.menu.MenuCreateForm;
import com.wow.delivery.dto.menu.MenuOrderUpdateDTO;
import com.wow.delivery.dto.menu.MenuResponse;
import com.wow.delivery.dto.menu.MenuUpdateForm;
import com.wow.delivery.entity.menu.MenuEntity;
import com.wow.delivery.entity.shop.ShopEntity;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.repository.MenuRepository;
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
public class MenuService {

    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final ImageService imageService;

    @Transactional
    public void createMenu(MenuCreateForm menuCreateForm) {
        ShopEntity shopEntity = shopRepository.findByIdOrThrow(menuCreateForm.getShopId(), ErrorCode.SHOP_DATA_NOT_FOUND, null);

        MenuEntity menuEntity = MenuEntity.builder()
            .shopId(shopEntity.getIdOrThrow())
            .menuCategoryId(menuCreateForm.getMenuCategoryId())
            .name(menuCreateForm.getName())
            .introduction(menuCreateForm.getIntroduction())
            .price(menuCreateForm.getPrice())
            .isSelling(menuCreateForm.isSelling())
            .build();
        MenuEntity saveMenuEntity = menuRepository.save(menuEntity);
        saveMenuEntity.createMenuOrder();
        saveMenuEntity.setImagePath(imageService.getImagePath(saveMenuEntity.getClass().getSimpleName(), saveMenuEntity.getIdOrThrow(),
            menuCreateForm.getFile(), menuCreateForm.getX(), menuCreateForm.getY(), menuCreateForm.getLength()));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> getMenus(Long shopId) {
        ShopEntity shopEntity = shopRepository.findByIdOrThrow(shopId, ErrorCode.SHOP_DATA_NOT_FOUND, null);
        List<MenuEntity> answer = menuRepository.findAllByShopIdOrderByMenuOrderAsc(shopEntity.getIdOrThrow());
        return answer.stream()
            .map(menuEntity -> MenuResponse.builder()
                .menuId(menuEntity.getIdOrThrow())
                .menuCategoryId(menuEntity.getMenuCategoryId())
                .name(menuEntity.getName())
                .introduction(menuEntity.getIntroduction())
                .price(menuEntity.getPrice())
                .imagePath(menuEntity.getImagePath())
                .menuOrder(menuEntity.getMenuOrder())
                .build())
            .toList();
    }

    @Transactional
    public void update(MenuUpdateForm menuUpdateForm) {
        MenuEntity menuEntity = menuRepository.findByIdOrThrow(menuUpdateForm.getMenuId(), ErrorCode.MENU_DATA_NOT_FOUND, null);
        menuEntity.update(
            menuUpdateForm.getMenuCategoryId(),
            menuUpdateForm.getName(),
            menuUpdateForm.getIntroduction(),
            menuUpdateForm.getPrice(),
            imageService.getImagePath(menuEntity.getClass().getSimpleName(), menuEntity.getIdOrThrow(), menuUpdateForm.getFile(), menuUpdateForm.getX(), menuUpdateForm.getY(), menuUpdateForm.getLength()),
            menuUpdateForm.isSelling()
        );
    }

    @Transactional
    public void reorderMenus(MenuOrderUpdateDTO updateDTO) {
        List<Long> beforeMenuIds = updateDTO.getBeforeMenuIds();
        List<Long> afterMenuIds = updateDTO.getAfterMenuIds();

        List<MenuEntity> menuEntities = menuRepository.findByIdIn(beforeMenuIds);

        Map<Long, MenuEntity> menuMap = menuEntities.stream()
            .collect(Collectors.toMap(MenuEntity::getId, menuEntity -> menuEntity));

        List<Integer> sortedMenuOrders = beforeMenuIds.stream()
            .map(id -> menuMap.get(id).getMenuOrder())
            .toList();

        IntStream.range(0, updateDTO.getSize())
            .filter(i -> !beforeMenuIds.get(i).equals(afterMenuIds.get(i)))
            .forEach(i -> {
                MenuEntity menuEntity = menuMap.get(afterMenuIds.get(i));
                menuEntity.setMenuOrder(sortedMenuOrders.get(i));
            });
    }
}
