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
public class ShopCategory extends BaseEntity {

    @Column
    private Long shopId;

    @Column
    private Long metaCategoryId;

    @Builder
    public ShopCategory(Long shopId, Long metaCategoryId) {
        this.shopId = shopId;
        this.metaCategoryId = metaCategoryId;
    }
}
