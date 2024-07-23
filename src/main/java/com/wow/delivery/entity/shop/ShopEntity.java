package com.wow.delivery.entity.shop;

import com.wow.delivery.entity.BaseEntity;
import com.wow.delivery.entity.common.Address;
import com.wow.delivery.util.SnowFlakeGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ShopEntity extends BaseEntity {

    @Column(name = "public_id") // 가게 고유 번호 uuid w-12sf113
    private String publicId;

    @Column(name = "owner_id")
    private Long ownerId;

    @Comment(value = "가게 명")
    @Column(name = "shop_name", columnDefinition = "VARCHAR(20)", nullable = false)
    private String shopName;

    @Comment(value = "가게 소개")
    @Column(name = "introduction", columnDefinition = "VARCHAR(255)")
    private String introduction;

    @Embedded
    private BusinessHours businessHours;

    @Embedded
    private Address address;

    @Column(name = "open_days")
    @Convert(converter = DayOfWeekListConverter.class)
    private List<DayOfWeek> openDays = new ArrayList<>();

    @Comment(value = "최소 주문 금액")
    @Column(name = "min_order_price", columnDefinition = "INT")
    private int minOrderPrice;

    @Comment(value = "배달비")
    @Column(name = "delivery_fee", columnDefinition = "INT")
    private int deliveryFee;

    @Embedded
    private S2LevelToken s2LevelToken;

    @Builder
    public ShopEntity(Long ownerId, String shopName, String introduction, BusinessHours businessHours, Address address, List<DayOfWeek> openDays, int minOrderPrice, int deliveryFee, S2LevelToken s2LevelToken) {
        this.publicId = SnowFlakeGenerator.generateBase36String();
        this.ownerId = ownerId;
        this.shopName = shopName;
        this.introduction = introduction;
        this.businessHours = businessHours;
        this.address = address;
        this.openDays = openDays;
        this.minOrderPrice = minOrderPrice;
        this.deliveryFee = deliveryFee;
        this.s2LevelToken = s2LevelToken;
    }

    public void update(String shopName, String introduction, BusinessHours businessHours, Address address, List<DayOfWeek> openDays, int minOrderPrice, int deliveryFee, S2LevelToken s2LevelToken) {
        this.shopName = shopName;
        this.introduction = introduction;
        this.businessHours = businessHours;
        this.address = address;
        this.openDays = openDays;
        this.minOrderPrice = minOrderPrice;
        this.deliveryFee = deliveryFee;
        this.s2LevelToken = s2LevelToken;
    }
}
