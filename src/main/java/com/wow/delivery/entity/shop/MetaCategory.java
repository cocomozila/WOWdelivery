package com.wow.delivery.entity.shop;

import com.wow.delivery.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MetaCategory extends BaseEntity {

    @Column(name = "category_name")
    private String categoryName;

    @Builder
    public MetaCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}
