package com.wow.delivery.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
public class PaymentResponse {

    @Comment(value = "유저 ID")
    private Long userId;

    @Comment(value = "지불 방법")
    private String payType;

    @Comment(value = "주문 고유 ID")
    private String transactionId;

    @Comment(value = "지불 금액")
    private Long amount;

    @Comment(value = "결제 성공 시 콜백 주소")
    private String successUrl;

    @Comment(value = "결제 실패 시 콜백 주소")
    private String failUrl;

    @Builder
    public PaymentResponse(Long userId, String payType, Long amount, String transactionId, String successUrl, String failUrl) {
        this.userId = userId;
        this.payType = payType;
        this.amount = amount;
        this.transactionId = transactionId;
        this.successUrl = successUrl;
        this.failUrl = failUrl;
    }
}
