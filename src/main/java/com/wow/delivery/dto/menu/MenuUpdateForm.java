package com.wow.delivery.dto.menu;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.web.multipart.MultipartFile;

import java.beans.ConstructorProperties;

@Getter
public class MenuUpdateForm {

    @Comment(value = "메뉴 카테고리")
    private Long menuCategoryId;

    @NotNull
    @Comment(value = "메뉴 ID")
    private Long menuId;

    @NotBlank
    @Comment(value = "메뉴 이름")
    private String name;

    @Comment(value = "메뉴 설명")
    private String introduction;

    @Min(0)
    @Comment(value = "메뉴 가격")
    private int price;

    @Comment(value = "판매 상태")
    private boolean isSelling;

    @Min(1)
    @Comment(value = "메뉴 순서")
    private int menuOrder;

    private MultipartFile file;

    @Builder
    @ConstructorProperties({"menuCategoryId", "menuId", "name", "introduction", "price", "isSelling", "file"})
    public MenuUpdateForm(Long menuCategoryId ,Long menuId, String name, String introduction, int price, boolean isSelling, MultipartFile file) {
        this.menuCategoryId = menuCategoryId;
        this.menuId = menuId;
        this.name = name;
        this.introduction = introduction;
        this.price = price;
        this.isSelling = isSelling;
        this.file = file;
    }
}
