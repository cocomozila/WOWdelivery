package com.wow.delivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Comment(value = "메뉴 이름")
    @Column(name = "name", columnDefinition = "VARCHAR(20)", nullable = false)
    private String name;

    @Comment(value = "메뉴 설명")
    @Column(name = "introduction", columnDefinition = "VARCHAR(255)", nullable = false)
    private String introduction;

    @Comment(value = "메뉴 가격")
    @Column(name = "price", columnDefinition = "INTEGER", nullable = false)
    private int price;

    @Comment(value = "메뉴 이미지 경로")
    @Column(name = "image_path", columnDefinition = "VARCHAR(255)", nullable = false)
    private String imagePath;

    @Comment(value = "판매 상태")
    @Column(name = "is_selling", columnDefinition = "BOOLEAN", nullable = false)
    private boolean isSelling;

    @Comment(value = "메뉴 순서")
    @Column(name = "menu_order", columnDefinition = "INTEGER", nullable = false)
    private int menuOrder;

    @Builder
    public Menu(Long shopId, String name, String introduction, int price, String imagePath, boolean isSelling, int menuOrder) {
        this.shopId = shopId;
        this.name = name;
        this.introduction = introduction;
        this.price = price;
        this.imagePath = imagePath;
        this.isSelling = isSelling;
        this.menuOrder = menuOrder;
    }

    public void update(String name, String introduction, int price, String imagePath, boolean isSelling, int menuOrder) {
        this.name = name;
        this.introduction = introduction;
        this.price = price;
        this.imagePath = imagePath;
        this.isSelling = isSelling;
        this.menuOrder = menuOrder;
    }
}
