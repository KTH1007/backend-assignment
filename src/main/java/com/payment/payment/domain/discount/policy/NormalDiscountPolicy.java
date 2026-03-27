package com.payment.payment.domain.discount.policy;

import com.payment.payment.domain.discount.model.DiscountPolicy;
import com.payment.payment.domain.member.model.MemberGrade;
import org.springframework.stereotype.Component;

@Component
public class NormalDiscountPolicy implements DiscountPolicy {

    @Override
    public boolean supports(MemberGrade grade) {
        return grade == MemberGrade.NORMAL;
    }

    @Override
    public int calculate(int originalPrice) {
        return originalPrice;
    }

    @Override
    public String getPolicyName() {
        return "NORMAL";
    }
}
