package com.wow.delivery.dto.menu.category;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Getter
public class MenuCategoryUpdateDTO {

    @NotNull
    @Comment(value = "메뉴 카테고리 ID")
    private Long menuCategoryId;

    @Comment(value = "메뉴 카테고리 ID")
    private String name;

    @Builder
    public MenuCategoryUpdateDTO(Long menuCategoryId, String name) {
        this.menuCategoryId = menuCategoryId;
        this.name = name;
    }
}
