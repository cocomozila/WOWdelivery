package com.wow.delivery.dto.menu.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuCategoryCreateDTO {

    @NotNull
    private Long shopId; // 가게 ID

    @NotBlank
    private String name; // 메뉴 카테고리 이름

    @Builder
    public MenuCategoryCreateDTO(Long shopId, String name) {
        this.shopId = shopId;
        this.name = name;
    }
}
