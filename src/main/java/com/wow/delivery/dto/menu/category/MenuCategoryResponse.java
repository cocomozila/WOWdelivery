package com.wow.delivery.dto.menu.category;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Getter
public class MenuCategoryResponse {

    @Comment("메뉴 카테고리 ID")
    private Long menuCategoryId;

    @Comment(value = "메뉴 카테고리 이름")
    private String name;

    @Comment(value = "순서")
    private int menuCategoryOrder;

    @Builder
    public MenuCategoryResponse(Long MenuCategoryId, String name, int menuCategoryOrder) {
        this.menuCategoryId = MenuCategoryId;
        this.name = name;
        this.menuCategoryOrder = menuCategoryOrder;
    }
}
