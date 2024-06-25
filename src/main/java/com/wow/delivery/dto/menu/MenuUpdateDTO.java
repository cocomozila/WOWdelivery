package com.wow.delivery.dto.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
public class MenuUpdateDTO {

    private Long menuId;

    @Comment(value = "메뉴 이름")
    private String name;

    @Comment(value = "메뉴 설명")
    private String introduction;

    @Comment(value = "메뉴 가격")
    private int price;

    @Comment(value = "판매 상태")
    private boolean isSelling;

    @Comment(value = "메뉴 순서")
    private int menuOrder;

    @Builder
    public MenuUpdateDTO(Long menuId, String name, String introduction, int price, boolean isSelling, int menuOrder) {
        this.menuId = menuId;
        this.name = name;
        this.introduction = introduction;
        this.price = price;
        this.isSelling = isSelling;
        this.menuOrder = menuOrder;
    }
}
