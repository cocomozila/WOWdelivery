package com.wow.delivery.controller;

import com.wow.delivery.dto.menu.MenuCreateDTO;
import com.wow.delivery.dto.menu.MenuRequest;
import com.wow.delivery.dto.menu.MenuResponse;
import com.wow.delivery.dto.menu.MenuUpdateDTO;
import com.wow.delivery.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<HttpStatus> register(@RequestPart("menuCreateDTO") @Valid MenuCreateDTO menuCreateDTO,
                                               @RequestPart("file") MultipartFile file) {
        menuService.createMenu(menuCreateDTO, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> getMenus(@RequestBody MenuRequest menuRequest) {
        return ResponseEntity.ok(menuService.getMenus(menuRequest));
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateMenu(@RequestPart MenuUpdateDTO menuUpdateDTO,
                                                 @RequestPart MultipartFile file) {
        menuService.update(menuUpdateDTO, file);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
