package com.wow.delivery.service;

import com.wow.delivery.dto.menu.MenuCreateDTO;
import com.wow.delivery.dto.menu.MenuRequest;
import com.wow.delivery.dto.menu.MenuResponse;
import com.wow.delivery.dto.menu.MenuUpdateDTO;
import com.wow.delivery.entity.Menu;
import com.wow.delivery.entity.shop.Shop;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.repository.MenuRepository;
import com.wow.delivery.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final ImageService imageService;

    @Transactional
    public void createMenu(MenuCreateDTO menuCreateDTO, MultipartFile file) {
        Shop shop = shopRepository.findByIdOrThrow(menuCreateDTO.getShopId(), ErrorCode.SHOP_DATA_NOT_FOUND, null);

        Menu menu = Menu.builder()
            .shopId(shop.getIdOrThrow())
            .name(menuCreateDTO.getName())
            .introduction(menuCreateDTO.getIntroduction())
            .price(menuCreateDTO.getPrice())
            .imagePath(imageService.getImagePath(file))
            .isSelling(menuCreateDTO.isSelling())
            .menuOrder(menuCreateDTO.getMenuOrder())
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
    public void update(MenuUpdateDTO menuUpdateDTO, MultipartFile file) {
        Menu menu = menuRepository.findByIdOrThrow(menuUpdateDTO.getMenuId(), ErrorCode.MENU_DATA_NOT_FOUND, null);
        menu.update(
            menuUpdateDTO.getName(),
            menuUpdateDTO.getIntroduction(),
            menuUpdateDTO.getPrice(),
            imageService.getImagePath(file),
            menuUpdateDTO.isSelling(),
            menuUpdateDTO.getMenuOrder()
        );
    }
}
