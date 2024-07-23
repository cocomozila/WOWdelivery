package com.wow.delivery.dto.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
public class PaymentRequest {

    @NotNull
    @Comment(value = "유저 ID")
    private Long userId;

    @NotNull
    @Comment(value = "지불 방법")
    private String payType;

    @Min(0)
    @Comment(value = "지불 금액")
    private Long amount;

    @Builder
    public PaymentRequest(Long userId, String payType, Long amount) {
        this.userId = userId;
        this.payType = payType;
        this.amount = amount;
    }
}
