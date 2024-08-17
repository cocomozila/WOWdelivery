package com.wow.delivery.dto.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuResponse {

    private Long menuId; // 메뉴 ID

    private Long menuCategoryId; // 메뉴 카테고리 ID

    private String name; // 메뉴 이름

    private String introduction; // 메뉴 설명

    private int price; // 메뉴 가격

    private String imagePath; // 메뉴 이미지 경로

    private int menuOrder; // 메뉴 순서

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
