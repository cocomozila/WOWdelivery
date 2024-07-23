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
public class ShopCategoryEntity extends BaseEntity {

    @Column(name = "shop_id")
    private Long shopId;

    @Column(name = "meta_category_id")
    private Long metaCategoryId;

    @Builder
    public ShopCategoryEntity(Long shopId, Long metaCategoryId) {
        this.shopId = shopId;
        this.metaCategoryId = metaCategoryId;
    }
}
