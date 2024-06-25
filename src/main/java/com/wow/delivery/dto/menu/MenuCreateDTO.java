package com.wow.delivery.dto.menu;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
public class MenuCreateDTO {

    @Comment(value = "가게 ID")
    @NotNull
    private Long shopId;

    @Comment(value = "메뉴 이름")
    @NotBlank
    private String name;

    @Comment(value = "메뉴 설명")
    private String introduction;

    @Comment(value = "메뉴 가격")
    @Min(0)
    private int price;

    @Comment(value = "판매 상태")
    private boolean isSelling;

    @Comment(value = "메뉴 순서")
    @Min(1)
    private int menuOrder;

    @Builder
    public MenuCreateDTO(Long shopId, String name, String introduction, int price, boolean isSelling, int menuOrder) {
        this.shopId = shopId;
        this.name = name;
        this.introduction = introduction;
        this.price = price;
        this.isSelling = isSelling;
        this.menuOrder = menuOrder;
    }
}
