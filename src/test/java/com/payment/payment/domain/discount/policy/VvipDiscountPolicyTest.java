package com.payment.payment.domain.discount.policy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class VvipDiscountPolicyTest {

    private final VvipDiscountPolicy policy = new VvipDiscountPolicy();

    @Test
    @DisplayName("VVIP 등급은 10% 할인이 적용된다")
    void calculate_rateDiscount() {
        int result = policy.calculate(10000);

        assertThat(result).isEqualTo(9000);
    }

    @Test
    @DisplayName("VVIP 등급은 정수 연산으로 할인이 적용된다")
    void calculate_integerArithmetic() {
        int result = policy.calculate(10001);

        assertThat(result).isEqualTo(9000);
    }
}