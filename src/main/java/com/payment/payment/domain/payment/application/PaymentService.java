package com.payment.payment.domain.payment.application;

import com.payment.payment.domain.discount.model.DiscountPolicy;
import com.payment.payment.domain.discount.resolver.DiscountPolicyResolver;
import com.payment.payment.domain.order.model.Order;
import com.payment.payment.domain.order.repository.OrderRepository;
import com.payment.payment.domain.payment.dto.request.PaymentRequest;
import com.payment.payment.domain.payment.dto.response.PaymentResponse;
import com.payment.payment.domain.payment.model.Payment;
import com.payment.payment.domain.payment.repository.PaymentRepository;
import com.payment.payment.global.exception.BusinessException;
import com.payment.payment.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final DiscountPolicyResolver discountPolicyResolver;
    private final Clock clock;

    @Transactional
    public PaymentResponse pay(PaymentRequest request) {
        Order order = orderRepository.findByIdWithLock(request.orderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        if (paymentRepository.existsByOrder(order)) {
            throw new BusinessException(ErrorCode.ALREADY_PAID);
        }

        DiscountPolicy policy = discountPolicyResolver.resolve(order.getMember().getGrade());
        int finalAmount = policy.calculate(order.getOriginalPrice());

        Payment payment = Payment.create(order, finalAmount, request.paymentMethod(), clock);
        paymentRepository.save(payment);

        return PaymentResponse.from(payment, order);
    }
}
