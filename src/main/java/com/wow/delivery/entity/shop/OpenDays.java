package com.wow.delivery.entity.shop;

import com.wow.delivery.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class OpenDays extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @Column(name = "open_days")
    @Convert(converter = DayOfWeekListConverter.class)
    private List<DayOfWeek> openDays = new ArrayList<>();

    @Builder
    public OpenDays(Shop shop, List<DayOfWeek> openDays) {
        this.shop = shop;
        this.openDays = openDays;
    }
}
