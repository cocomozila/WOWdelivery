package com.wow.delivery.entity.shop;

import com.wow.delivery.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "meta_categories")
public class MetaCategoryEntity extends BaseEntity {

    @Comment(value = "카테고리 명")
    @Column(name = "category_name")
    private String categoryName;

    @Builder
    public MetaCategoryEntity(String categoryName) {
        this.categoryName = categoryName;
    }
}
