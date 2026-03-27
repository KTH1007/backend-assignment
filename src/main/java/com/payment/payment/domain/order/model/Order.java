package com.payment.payment.domain.order.model;

import com.payment.payment.domain.member.model.Member;
import com.payment.payment.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int originalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    private Order(String productName, int originalPrice, Member member) {
        this.productName = productName;
        this.originalPrice = originalPrice;
        this.member = member;
    }

    public static Order create(String productName, int originalPrice, Member member) {
        return Order.builder()
                .productName(productName)
                .originalPrice(originalPrice)
                .member(member)
                .build();
    }
}
