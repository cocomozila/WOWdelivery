package com.wow.delivery.dto.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
public class MenuResponse {

    @Comment(value = "메뉴 ID")
    private Long menuId;

    @Comment(value = "메뉴 카테고리 ID")
    private Long menuCategoryId;

    @Comment(value = "메뉴 이름")
    private String name;

    @Comment(value = "메뉴 설명")
    private String introduction;

    @Comment(value = "메뉴 가격")
    private int price;

    @Comment(value = "메뉴 이미지 경로")
    private String imagePath;

    @Comment(value = "메뉴 순서")
    private int menuOrder;

    @Builder
    public MenuResponse(Long menuId, Long menuCategoryId, String name, String introduction, int price, String imagePath, int menuOrder) {
        this.menuId = menuId;
        this.menuCategoryId = menuCategoryId;
        this.name = name;
        this.introduction = introduction;
        this.price = price;
        this.imagePath = imagePath;
        this.menuOrder = menuOrder;
    }
}
