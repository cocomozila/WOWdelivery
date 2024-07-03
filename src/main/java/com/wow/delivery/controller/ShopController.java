package com.wow.delivery.controller;

import com.wow.delivery.dto.menu.category.MenuCategoryCreateDTO;
import com.wow.delivery.dto.menu.category.MenuCategoryOrderUpdateDTO;
import com.wow.delivery.dto.menu.category.MenuCategoryResponse;
import com.wow.delivery.dto.menu.category.MenuCategoryUpdateDTO;
import com.wow.delivery.dto.shop.*;
import com.wow.delivery.service.MenuCategoryService;
import com.wow.delivery.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final MenuCategoryService menuCategoryService;

    @PostMapping
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid ShopCreateDTO shopCreateDTO) {
        shopService.createShop(shopCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/category")
    public ResponseEntity<List<NearbyShopResponse>> getCategoryNearByShops(@RequestBody CategoryNearbyShopRequestDTO requestDTO) {
        return ResponseEntity.ok(shopService.getShopsByCategory(requestDTO));
    }

    @GetMapping("/name")
    public ResponseEntity<List<NearbyShopResponse>> getNameNearByShops(@RequestBody NameNearbyShopRequestDTO requestDTO) {
        return ResponseEntity.ok(shopService.getShopsByShopName(requestDTO));
    }

    @GetMapping("/{shopId}")
    public ResponseEntity<ShopResponse> getShop(@PathVariable("shopId") Long shopId) {
        return ResponseEntity.ok(shopService.getShop(shopId));
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateAddress(@RequestBody @Valid ShopUpdateDTO shopUpdateDTO) {
        shopService.updateShop(shopUpdateDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/menu-category")
    public ResponseEntity<HttpStatus> registerMenuCategory(@RequestBody @Valid MenuCategoryCreateDTO createDTO) {
        menuCategoryService.createMenuCategory(createDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{shopId}/menu-category")
    public ResponseEntity<List<MenuCategoryResponse>> getMenuCategory(@PathVariable("shopId") Long shopId) {
        return ResponseEntity.ok(menuCategoryService.getMenuCategory(shopId));
    }

    @PutMapping("/menu-category/reorder")
    public ResponseEntity<HttpStatus> reorderMenuCategories(@RequestBody @Valid MenuCategoryOrderUpdateDTO updateDTO) {
        menuCategoryService.reorderMenuCategories(updateDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/menu-category")
    public ResponseEntity<HttpStatus> reorderMenuCategories(@RequestBody @Valid MenuCategoryUpdateDTO updateDTO) {
        menuCategoryService.updateMenuCategory(updateDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
