package com.wow.delivery.entity.order;

import com.wow.delivery.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_details")
public class OrderDetailsEntity extends BaseEntity {

    @Comment(value = "주문 ID")
    private Long orderId;

    @Comment(value = "메뉴 ID")
    private Long menuId;

    @Comment(value = "수량")
    private int amount;

    @Builder
    public OrderDetailsEntity(Long orderId, Long menuId, int amount) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.amount = amount;
    }
}
