package com.wow.delivery.entity;

import com.wow.delivery.entity.common.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class DeliveryAddress extends BaseEntity {

    @Column(name = "user_id")
    private Long userId; // userId로 수정

    @Column(name = "address_alias", columnDefinition = "VARCHAR(20)")
    private String addressAlias; // 주소 별명

    @Embedded
    private Address address;

    @Builder
    public DeliveryAddress(Long userId, String addressAlias, Address address) {
        this.userId = userId;
        this.addressAlias = addressAlias;
        this.address = address;
    }

    public void update(String addressAlias, Address address) {
        this.addressAlias = addressAlias;
        this.address = address;
    }
}
