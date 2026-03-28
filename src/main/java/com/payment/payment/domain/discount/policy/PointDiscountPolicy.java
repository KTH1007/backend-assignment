package com.payment.payment.domain.discount.policy;

import com.payment.payment.domain.discount.model.PaymentMethodDiscountPolicy;
import com.payment.payment.domain.payment.model.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PointDiscountPolicy implements PaymentMethodDiscountPolicy {

    private static final int DISCOUNT_RATE = 5;

    @Override
    public boolean supports(PaymentMethod paymentMethod) {
        return paymentMethod == PaymentMethod.POINT;
    }

    @Override
    public int calculate(int amount) {
        return amount * (100 - DISCOUNT_RATE) / 100;
    }

    @Override
    public int getDiscountRate() {
        return DISCOUNT_RATE;
    }

    @Override
    public String getPolicyName() {
        return "POINT_RATE_" + DISCOUNT_RATE;
    }
}
