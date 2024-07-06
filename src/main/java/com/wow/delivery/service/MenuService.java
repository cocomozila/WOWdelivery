package com.wow.delivery.service;

import com.wow.delivery.dto.menu.MenuCreateForm;
import com.wow.delivery.dto.menu.MenuOrderUpdateDTO;
import com.wow.delivery.dto.menu.MenuResponse;
import com.wow.delivery.dto.menu.MenuUpdateForm;
import com.wow.delivery.entity.menu.Menu;
import com.wow.delivery.entity.shop.Shop;
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
        Shop shop = shopRepository.findByIdOrThrow(menuCreateForm.getShopId(), ErrorCode.SHOP_DATA_NOT_FOUND, null);

        Menu menu = Menu.builder()
            .shopId(shop.getIdOrThrow())
            .menuCategoryId(menuCreateForm.getMenuCategoryId())
            .name(menuCreateForm.getName())
            .introduction(menuCreateForm.getIntroduction())
            .price(menuCreateForm.getPrice())
            .isSelling(menuCreateForm.isSelling())
            .build();
        Menu saveMenu = menuRepository.save(menu);
        saveMenu.createMenuOrder();
        saveMenu.setImagePath(imageService.getImagePath(saveMenu.getClass().getSimpleName(), saveMenu.getIdOrThrow(),
            menuCreateForm.getFile(), menuCreateForm.getX(), menuCreateForm.getY(), menuCreateForm.getLength()));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> getMenus(Long shopId) {
        Shop shop = shopRepository.findByIdOrThrow(shopId, ErrorCode.SHOP_DATA_NOT_FOUND, null);
        List<Menu> answer = menuRepository.findAllByShopIdOrderByOrder(shop.getIdOrThrow());
        return answer.stream()
            .map(menu -> MenuResponse.builder()
                .menuId(menu.getIdOrThrow())
                .menuCategoryId(menu.getMenuCategoryId())
                .name(menu.getName())
                .introduction(menu.getIntroduction())
                .price(menu.getPrice())
                .imagePath(menu.getImagePath())
                .menuOrder(menu.getMenuOrder())
                .build())
            .toList();
    }

    @Transactional
    public void update(MenuUpdateForm menuUpdateForm) {
        Menu menu = menuRepository.findByIdOrThrow(menuUpdateForm.getMenuId(), ErrorCode.MENU_DATA_NOT_FOUND, null);
        menu.update(
            menuUpdateForm.getMenuCategoryId(),
            menuUpdateForm.getName(),
            menuUpdateForm.getIntroduction(),
            menuUpdateForm.getPrice(),
            imageService.getImagePath(menu.getClass().getSimpleName(), menu.getIdOrThrow(), menuUpdateForm.getFile(), menuUpdateForm.getX(), menuUpdateForm.getY(), menuUpdateForm.getLength()),
            menuUpdateForm.isSelling()
        );
    }

    @Transactional
    public void reorderMenus(MenuOrderUpdateDTO updateDTO) {
        List<Long> beforeMenuIds = updateDTO.getBeforeMenuIds();
        List<Long> afterMenuIds = updateDTO.getAfterMenuIds();

        List<Menu> menus = menuRepository.findByIdIn(beforeMenuIds);

        Map<Long, Menu> menuMap = menus.stream()
            .collect(Collectors.toMap(Menu::getId, menu -> menu));

        List<Integer> sortedMenuOrders = beforeMenuIds.stream()
            .map(id -> menuMap.get(id).getMenuOrder())
            .toList();

        IntStream.range(0, updateDTO.getSize())
            .filter(i -> !beforeMenuIds.get(i).equals(afterMenuIds.get(i)))
            .forEach(i -> {
                Menu menu = menuMap.get(afterMenuIds.get(i));
                menu.setMenuOrder(sortedMenuOrders.get(i));
            });
    }
}
