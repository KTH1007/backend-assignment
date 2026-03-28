    package com.payment.payment.domain.discount.policy;

    import com.payment.payment.domain.discount.model.DiscountPolicy;
    import com.payment.payment.domain.member.model.MemberGrade;
    import org.springframework.stereotype.Component;

    @Component
    public class VvipDiscountPolicy implements DiscountPolicy {

        private static final int DISCOUNT_RATE = 10;

        @Override
        public boolean supports(MemberGrade grade) {
            return grade == MemberGrade.VVIP;
        }

        @Override
        public int calculate(int originalPrice) {
            return originalPrice * (100 - DISCOUNT_RATE) / 100;
        }

        @Override
        public String getPolicyName() {
            return "VVIP_RATE_" + DISCOUNT_RATE;
        }

        @Override
        public int getDiscountRate(int originalPrice) {
            return DISCOUNT_RATE;
        }
    }