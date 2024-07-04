package com.wow.delivery.controller;

import com.wow.delivery.dto.menu.category.MenuCategoryCreateDTO;
import com.wow.delivery.dto.menu.category.MenuCategoryOrderUpdateDTO;
import com.wow.delivery.dto.menu.category.MenuCategoryResponse;
import com.wow.delivery.dto.menu.category.MenuCategoryUpdateDTO;
import com.wow.delivery.service.MenuCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuCategoryController {

    private final MenuCategoryService menuCategoryService;

    @PostMapping("/menu-categories")
    public ResponseEntity<HttpStatus> registerMenuCategory(@RequestBody @Valid MenuCategoryCreateDTO createDTO) {
        menuCategoryService.createMenuCategory(createDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/shops/{shopId}/menu-categories")
    public ResponseEntity<List<MenuCategoryResponse>> getAllMenuCategory(@PathVariable("shopId") Long shopId) {
        return ResponseEntity.ok(menuCategoryService.getMenuCategory(shopId));
    }

    @PutMapping("/menu-categories/reorder")
    public ResponseEntity<HttpStatus> reorderMenuCategories(@RequestBody @Valid MenuCategoryOrderUpdateDTO updateDTO) {
        menuCategoryService.reorderMenuCategories(updateDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/menu-categories")
    public ResponseEntity<HttpStatus> reorderMenuCategories(@RequestBody @Valid MenuCategoryUpdateDTO updateDTO) {
        menuCategoryService.updateMenuCategory(updateDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
