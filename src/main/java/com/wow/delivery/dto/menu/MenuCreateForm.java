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
public class MenuCreateForm {

    @NotNull
    @Comment(value = "가게 ID")
    private Long shopId;

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

    @Comment(value = "메뉴 이미지")
    private MultipartFile file;

    @Builder
    @ConstructorProperties({"shopId", "name", "introduction", "price", "isSelling", "menuOrder", "file"})
    public MenuCreateForm(Long shopId, String name, String introduction, int price, boolean isSelling, int menuOrder, MultipartFile file) {
        this.shopId = shopId;
        this.name = name;
        this.introduction = introduction;
        this.price = price;
        this.isSelling = isSelling;
        this.menuOrder = menuOrder;
        this.file = file;
    }
}
