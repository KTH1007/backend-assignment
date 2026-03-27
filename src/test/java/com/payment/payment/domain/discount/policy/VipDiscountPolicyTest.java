package com.payment.payment.domain.discount.policy;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class VipDiscountPolicyTest {

    private final VipDiscountPolicy policy = new VipDiscountPolicy();

    @Test
    @DisplayName("VIP 등급은 1,000원 고정 할인이 적용된다")
    void calculate_fixedDiscount() {
        int result = policy.calculate(10000);

        assertThat(result).isEqualTo(9000);
    }

    @Test
    @DisplayName("VIP 등급은 주문 금액이 1,000원 미만이면 0원이 된다")
    void calculate_priceUnderDiscountAmount() {
        int result = policy.calculate(500);

        assertThat(result).isEqualTo(0);
    }

}