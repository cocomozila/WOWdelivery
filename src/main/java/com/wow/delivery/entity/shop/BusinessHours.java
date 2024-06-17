package com.wow.delivery.entity.shop;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Embeddable
@NoArgsConstructor
public class BusinessHours {

    @Comment(value = "영업시작 시간")
    @Column(name = "open_time", columnDefinition = "VARCHAR(6)", nullable = false)
    private String openTime;

    @Comment(value = "영업종료 시간")
    @Column(name = "close_time", columnDefinition = "VARCHAR(6)", nullable = false)
    private String closeTime;

    @Builder
    public BusinessHours(String openTime, String closeTime) {
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
