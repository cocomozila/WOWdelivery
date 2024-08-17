package com.wow.delivery.dto.menu;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.beans.ConstructorProperties;

@Getter
public class MenuCreateForm {

    @NotNull
    private Long shopId; // 가게 ID

    @NotNull
    private Long menuCategoryId; // 메뉴 카테고리 ID

    @NotBlank
    private String name; // 메뉴 이름

    private String introduction; // 메뉴 설명

    @Min(0)
    private int price; // 메뉴 가격

    private boolean isSelling; // 판매 상태

    private MultipartFile file; // 메뉴 이미지

    private int x; // 이미지 자를 중심좌표 x값

    private int y; // 이미지 자를 중심좌표 y값

    private int length; // 이미지 자를 정사각형 한 변의 사이즈

    @Builder
    @ConstructorProperties({"shopId", "menuCategoryId", "name", "introduction", "price", "isSelling", "file", "x", "y", "length"})
    public MenuCreateForm(Long shopId, Long menuCategoryId, String name, String introduction, int price, boolean isSelling, MultipartFile file, int x, int y, int length) {
        this.shopId = shopId;
        this.menuCategoryId = menuCategoryId;
        this.name = name;
        this.introduction = introduction;
        this.price = price;
        this.isSelling = isSelling;
        this.file = file;
        this.x = x;
        this.y = y;
        this.length = length;
    }
}
