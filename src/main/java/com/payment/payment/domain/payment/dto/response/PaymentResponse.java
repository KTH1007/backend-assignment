package com.payment.payment.domain.payment.dto.response;

import com.payment.payment.domain.payment.model.Payment;
import com.payment.payment.domain.payment.model.PaymentMethod;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(

        UUID paymentId,
        UUID orderId,
        String productName,
        int originalPrice,
        int finalAmount,
        PaymentMethod paymentMethod,
        LocalDateTime paidAt
) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getOrder().getProductName(),
                payment.getOrder().getOriginalPrice(),
                payment.getFinalAmount(),
                payment.getPaymentMethod(),
                payment.getPaidAt()
        );
    }
}