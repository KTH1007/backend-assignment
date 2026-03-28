package com.payment.payment.domain.discount.history.model;

import com.payment.payment.domain.payment.model.Payment;
import com.payment.payment.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "discount_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiscountHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false, updatable = false)
    private Payment payment;

    @Column(nullable = false, updatable = false)
    private String memberGrade;

    @Column(nullable = false, updatable = false)
    private String policyName;

    @Column(nullable = false, updatable = false)
    private int discountAmount;

    @Column(nullable = false, updatable = false)
    private int discountRate;

    @Builder
    private DiscountHistory(Payment payment, String memberGrade, String policyName,
                            int discountAmount, int discountRate) {
        this.payment = payment;
        this.memberGrade = memberGrade;
        this.policyName = policyName;
        this.discountAmount = discountAmount;
        this.discountRate = discountRate;
    }

    public static DiscountHistory create(Payment payment, String memberGrade,
                                         String policyName, int discountAmount, int discountRate) {
        return DiscountHistory.builder()
                .payment(payment)
                .memberGrade(memberGrade)
                .policyName(policyName)
                .discountAmount(discountAmount)
                .discountRate(discountRate)
                .build();
    }
}