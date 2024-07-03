package com.wow.delivery.controller;

import com.wow.delivery.dto.menu.MenuCreateForm;
import com.wow.delivery.dto.menu.MenuOrderUpdateDTO;
import com.wow.delivery.dto.menu.MenuUpdateForm;
import com.wow.delivery.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<HttpStatus> register(@ModelAttribute @Valid MenuCreateForm menuCreateForm) {
        menuService.createMenu(menuCreateForm);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateMenu(@ModelAttribute @Valid MenuUpdateForm menuUpdateForm) {
        menuService.update(menuUpdateForm);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/reorder")
    public ResponseEntity<HttpStatus> reorderMenus(@RequestBody MenuOrderUpdateDTO updateDTO) {
        menuService.reorderMenus(updateDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
