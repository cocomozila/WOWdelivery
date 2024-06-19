package com.wow.delivery.entity;

import com.wow.delivery.entity.common.Address;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class DeliveryAddress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "address_alias", columnDefinition = "VARCHAR(20)")
    private String addressAlias; // 주소 별명

    @Embedded
    private Address address;

    @Builder
    public DeliveryAddress(User user, String addressAlias, Address address) {
        this.user = user;
        this.addressAlias = addressAlias;
        this.address = address;
    }

    public void update(String addressAlias, Address address) {
        this.addressAlias = addressAlias;
        this.address = address;
    }
}
