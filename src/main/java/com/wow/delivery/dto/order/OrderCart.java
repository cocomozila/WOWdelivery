package com.wow.delivery.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
public class OrderCart {

    @Comment(value = "메뉴 ID")
    private Long menuId;

    @Comment(value = "메뉴 이름")
    private String menuName; // 삭제

    @Comment(value = "메뉴 가격")
    private Long price; // 삭제

    @Comment(value = "메뉴 수량")
    private int amount;

    @Builder
    public OrderCart(Long menuId, String menuName, Long price, int amount) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
        this.amount = amount;
    }
}
