package com.wow.delivery.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCart {

    private Long menuId; // 메뉴 ID

    private String menuName; // 메뉴 이름

    private Long price; // 메뉴 가격

    private int amount; // 메뉴 수량

    @Builder
    public OrderCart(Long menuId, String menuName, Long price, int amount) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
        this.amount = amount;
    }
}
