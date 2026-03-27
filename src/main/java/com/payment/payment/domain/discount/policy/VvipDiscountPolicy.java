package com.payment.payment.domain.discount.policy;

import com.payment.payment.domain.discount.model.DiscountPolicy;
import com.payment.payment.domain.member.model.MemberGrade;
import org.springframework.stereotype.Component;

@Component
public class VvipDiscountPolicy implements DiscountPolicy {

    private static final double DISCOUNT_RATE = 0.10;

    @Override
    public boolean supports(MemberGrade grade) {
        return grade == MemberGrade.VVIP;
    }

    @Override
    public int calculate(int originalPrice) {
        return (int) (originalPrice * (1 - DISCOUNT_RATE));
    }

    @Override
    public String getPolicyName() {
        return "VVIP_RATE_10";
    }
}