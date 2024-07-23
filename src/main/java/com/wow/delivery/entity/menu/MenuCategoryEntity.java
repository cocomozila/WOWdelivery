package com.wow.delivery.entity.menu;

import com.wow.delivery.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "menu_category")
public class MenuCategoryEntity extends BaseEntity {

    @Comment("가게 ID")
    private Long shopId;

    @Comment(value = "메뉴 카테고리 이름")
    @Column(name = "name", columnDefinition = "VARCHAR(20)", nullable = false)
    private String name;

    @Setter
    @Comment(value = "순서")
    @Column(name = "\"order\"", columnDefinition = "INTEGER", nullable = false)
    private int menuCategoryOrder;

    @Builder
    public MenuCategoryEntity(Long shopId, String name) {
        this.shopId = shopId;
        this.name = name;
    }

    public void createCategoryOrder() {
        this.menuCategoryOrder = this.getId().intValue();
    }

    public void update(String name) {
        this.name = name;
    }
}
