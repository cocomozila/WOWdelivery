package com.wow.delivery.service;

import com.wow.delivery.dto.menu.MenuCreateForm;
import com.wow.delivery.dto.menu.MenuRequest;
import com.wow.delivery.dto.menu.MenuResponse;
import com.wow.delivery.dto.menu.MenuUpdateForm;
import com.wow.delivery.entity.Menu;
import com.wow.delivery.entity.shop.Shop;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.repository.MenuRepository;
import com.wow.delivery.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            .name(menuCreateForm.getName())
            .introduction(menuCreateForm.getIntroduction())
            .price(menuCreateForm.getPrice())
            .imagePath(imageService.getImagePath(menuCreateForm.getFile()))
            .isSelling(menuCreateForm.isSelling())
            .menuOrder(menuCreateForm.getMenuOrder())
            .build();
        menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> getMenus(MenuRequest menuRequest) {
        Shop shop = shopRepository.findByIdOrThrow(menuRequest.getShopId(), ErrorCode.SHOP_DATA_NOT_FOUND, null);
        return menuRepository.findAllByIdOrderByMenuOrderAsc(shop.getIdOrThrow()).stream()
            .map(menu -> MenuResponse.builder()
                .menuId(menu.getIdOrThrow())
                .name(menu.getName())
                .introduction(menu.getIntroduction())
                .price(menu.getPrice())
                .imagePath(menu.getImagePath())
                .build())
            .toList();
    }

    @Transactional
    public void update(MenuUpdateForm menuUpdateForm) {
        Menu menu = menuRepository.findByIdOrThrow(menuUpdateForm.getMenuId(), ErrorCode.MENU_DATA_NOT_FOUND, null);
        menu.update(
            menuUpdateForm.getName(),
            menuUpdateForm.getIntroduction(),
            menuUpdateForm.getPrice(),
            imageService.getImagePath(menuUpdateForm.getFile()),
            menuUpdateForm.isSelling(),
            menuUpdateForm.getMenuOrder()
        );
    }
}
