package com.wow.delivery.dto.menu.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
public class MenuCategoryCreateDTO {

    @NotNull
    @Comment("가게 ID")
    private Long shopId;

    @NotBlank
    @Comment(value = "메뉴 카테고리 이름")
    private String name;

    @Builder
    public MenuCategoryCreateDTO(Long shopId, String name) {
        this.shopId = shopId;
        this.name = name;
    }
}
