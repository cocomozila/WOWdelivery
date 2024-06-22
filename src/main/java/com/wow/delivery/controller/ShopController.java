package com.wow.delivery.controller;

import com.wow.delivery.dto.shop.*;
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


    @PutMapping
    public ResponseEntity<HttpStatus> updateAddress(@RequestBody @Valid ShopUpdateDTO shopUpdateDTO) {
        shopService.updateShop(shopUpdateDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
