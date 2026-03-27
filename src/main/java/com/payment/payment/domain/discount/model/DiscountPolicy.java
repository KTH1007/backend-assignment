package com.payment.payment.domain.discount.model;

import com.payment.payment.domain.member.model.MemberGrade;

public interface DiscountPolicy {

    boolean supports(MemberGrade grade);

    int calculate(int originalPrice);

    String getPolicyName();
}