package com.wow.delivery.entity.shop;

import com.wow.delivery.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
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

    @Column(name = "shop_id")
    private Long shopId;

    @Column(name = "open_days")
    @Convert(converter = DayOfWeekListConverter.class)
    private List<DayOfWeek> openDays = new ArrayList<>();

    @Builder
    public OpenDays(Long shopId, List<DayOfWeek> openDays) {
        this.shopId = shopId;
        this.openDays = openDays;
    }
}
