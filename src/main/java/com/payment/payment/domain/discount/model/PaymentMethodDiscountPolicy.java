package com.payment.payment.domain.discount.model;

import com.payment.payment.domain.payment.model.PaymentMethod;

public interface PaymentMethodDiscountPolicy {

    boolean supports(PaymentMethod paymentMethod);

    int calculate(int amount);

    int getDiscountRate();

    String getPolicyName();
}