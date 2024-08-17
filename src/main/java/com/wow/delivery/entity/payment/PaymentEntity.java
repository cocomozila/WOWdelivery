package com.wow.delivery.entity.payment;

import com.wow.delivery.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "payments")
public class PaymentEntity extends BaseEntity {

    @Comment(value = "유저 ID")
    private Long userId;

    @Column(name = "transaction_id", columnDefinition = "VARCHAR(100)", nullable = false)
    @Comment(value = "거래 UUID")
    private String transactionId;

    @Comment(value = "지불 방법")
    @Column(name = "pay_type", columnDefinition = "VARCHAR(20)", nullable = false)
    private String payType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(20)", nullable = false)
    @Comment(value = "지불 상태")
    private PaymentStatus status;

    @Column(name = "amount", nullable = false)
    @Comment(value = "지불 금액")
    private Long amount;

    @Setter
    @Comment(value = "페이먼츠 발급 키")
    @Column(name = "payment_key", columnDefinition = "VARCHAR(200)")
    private String paymentKey;

    @Builder
    public PaymentEntity(Long userId, String payType, Long amount) {
        this.userId = userId;
        this.transactionId = UUID.randomUUID().toString();
        this.status = PaymentStatus.PENDING;
        this.payType = payType;
        this.amount = amount;
    }

    public void updateStatus(PaymentStatus status) {
        this.status = status;
    }
}
