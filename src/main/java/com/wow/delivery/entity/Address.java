package com.wow.delivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Address extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "address_alias", columnDefinition = "VARCHAR(20)")
    private String addressAlias; // 주소 별명

    @Column(name = "address_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String addressName; // 주소명

    @Column(name = "detailed_address", columnDefinition = "VARCHAR(30)")
    private String detailedAddress; // 상세주소

    @Column(name = "x_coordinate", columnDefinition = "DOUBLE", nullable = false)
    private Double xCoordinate; // x좌표

    @Column(name = "y_coordinate", columnDefinition = "DOUBLE", nullable = false)
    private Double yCoordinate; // y좌표

}
