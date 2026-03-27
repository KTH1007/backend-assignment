package com.payment.payment.domain.discount.resolver;

import com.payment.payment.domain.discount.model.DiscountPolicy;
import com.payment.payment.domain.member.model.MemberGrade;
import com.payment.payment.global.exception.BusinessException;
import com.payment.payment.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiscountPolicyResolver {

    private final List<DiscountPolicy> discountPolicies;

    // 등급에 맞는 할인 정책 선택
    public DiscountPolicy resolve(MemberGrade grade) {
        return discountPolicies.stream()
                .filter(policy -> policy.supports(grade))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.DISCOUNT_POLICY_NOT_FOUND));
    }
}
