package com.wow.delivery.entity.shop;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class BusinessHours {

    @Column(name = "open_time", columnDefinition = "VARCHAR(6)", nullable = false)
    private String openTime; // 영업시작 시간

    @Column(name = "close_time", columnDefinition = "VARCHAR(6)", nullable = false)
    private String closeTime; // 영업종료 시간

    @Builder
    public BusinessHours(String openTime, String closeTime) {
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
