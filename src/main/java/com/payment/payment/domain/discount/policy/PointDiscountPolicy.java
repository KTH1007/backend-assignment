package com.payment.payment.domain.discount.policy;

import org.springframework.stereotype.Component;

@Component
public class PointDiscountPolicy {

    private static final int DISCOUNT_RATE = 5;

    public int calculate(int amount) {
        return amount * (100 - DISCOUNT_RATE) / 100;
    }

    public int getDiscountRate() {
        return DISCOUNT_RATE;
    }

    public String getPolicyName() {
        return "POINT_RATE_" + DISCOUNT_RATE;
    }
}
