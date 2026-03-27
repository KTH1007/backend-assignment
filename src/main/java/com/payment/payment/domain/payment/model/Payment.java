package com.payment.payment.domain.payment.model;

import com.payment.payment.domain.order.model.Order;
import com.payment.payment.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false)
    private int finalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false, updatable = false)
    private LocalDateTime paidAt;

    @Builder
    private Payment(Order order, int finalAmount, PaymentMethod paymentMethod, LocalDateTime paidAt) {
        this.order = order;
        this.finalAmount = finalAmount;
        this.paymentMethod = paymentMethod;
        this.paidAt = paidAt;
    }

    public static Payment create(Order order, int finalAmount, PaymentMethod paymentMethod) {
        return Payment.builder()
                .order(order)
                .finalAmount(finalAmount)
                .paymentMethod(paymentMethod)
                .paidAt(LocalDateTime.now())
                .build();
    }
}