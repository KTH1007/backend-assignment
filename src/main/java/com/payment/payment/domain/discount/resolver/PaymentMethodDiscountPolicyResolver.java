package com.payment.payment.domain.discount.resolver;

import com.payment.payment.domain.discount.model.PaymentMethodDiscountPolicy;
import com.payment.payment.domain.payment.model.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentMethodDiscountPolicyResolver {

    private final List<PaymentMethodDiscountPolicy> policies;

    public Optional<PaymentMethodDiscountPolicy> resolve(PaymentMethod paymentMethod) {
        return policies.stream()
                .filter(policy -> policy.supports(paymentMethod))
                .findFirst();
    }
}