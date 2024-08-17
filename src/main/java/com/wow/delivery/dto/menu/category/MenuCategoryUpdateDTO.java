package com.wow.delivery.dto.menu.category;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MenuCategoryUpdateDTO {

    @NotNull
    private Long menuCategoryId; // 메뉴 카테고리 ID

    private String name; // 메뉴 이름

    @Builder
    public MenuCategoryUpdateDTO(Long menuCategoryId, String name) {
        this.menuCategoryId = menuCategoryId;
        this.name = name;
    }
}
