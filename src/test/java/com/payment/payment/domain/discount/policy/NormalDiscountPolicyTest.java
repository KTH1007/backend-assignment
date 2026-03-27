package com.payment.payment.domain.discount.policy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NormalDiscountPolicyTest {

    private final NormalDiscountPolicy policy = new NormalDiscountPolicy();

    @Test
    @DisplayName("NORMAL 등급은 할인이 적용되지 않는다.")
    void calculate_noDiscount() {
        int result = policy.calculate(10000);

        Assertions.assertThat(result).isEqualTo(10000);
    }
}