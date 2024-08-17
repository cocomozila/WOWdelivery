package com.wow.delivery.dto.menu.category;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MenuCategoryResponse {

    private Long menuCategoryId; // 메뉴 카테고리 ID

    private String name; // 메뉴 카테고리 이름

    private int menuCategoryOrder; // 순서

    @Builder
    public MenuCategoryResponse(Long MenuCategoryId, String name, int menuCategoryOrder) {
        this.menuCategoryId = MenuCategoryId;
        this.name = name;
        this.menuCategoryOrder = menuCategoryOrder;
    }
}
