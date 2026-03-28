package com.payment.payment.domain.discount.policy;

import com.payment.payment.domain.discount.model.DiscountPolicy;
import com.payment.payment.domain.member.model.MemberGrade;
import org.springframework.stereotype.Component;

@Component
public class VipDiscountPolicy implements DiscountPolicy {

    private static final int DISCOUNT_AMOUNT = 1000;

    @Override
    public boolean supports(MemberGrade grade) {
        return grade == MemberGrade.VIP;
    }

    @Override
    public int calculate(int originalPrice) {
        return Math.max(0, originalPrice - DISCOUNT_AMOUNT);
    }

    @Override
    public String getPolicyName() {
        return "VIP_FIXED_" + DISCOUNT_AMOUNT;
    }

    @Override
    public int getDiscountRate(int originalPrice) {
        return originalPrice > 0 ? DISCOUNT_AMOUNT * 100 / originalPrice : 0;
    }
}