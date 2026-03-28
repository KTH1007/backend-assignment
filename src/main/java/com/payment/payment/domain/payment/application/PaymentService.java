package com.payment.payment.domain.payment.application;

import com.payment.payment.domain.discount.history.application.DiscountHistoryService;
import com.payment.payment.domain.discount.model.DiscountPolicy;
import com.payment.payment.domain.discount.model.PaymentMethodDiscountPolicy;
import com.payment.payment.domain.discount.resolver.DiscountPolicyResolver;
import com.payment.payment.domain.discount.resolver.PaymentMethodDiscountPolicyResolver;
import com.payment.payment.domain.member.model.MemberGrade;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final DiscountPolicyResolver discountPolicyResolver;
    private final DiscountHistoryService discountHistoryService;
    private final Clock clock;
    private final PaymentMethodDiscountPolicyResolver paymentMethodDiscountPolicyResolver;

    @Transactional
    public PaymentResponse pay(PaymentRequest request) {
        Order order = orderRepository.findByIdWithLock(request.orderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        if (paymentRepository.existsByOrder(order)) {
            throw new BusinessException(ErrorCode.ALREADY_PAID);
        }

        MemberGrade grade = order.getMember().getGrade();
        DiscountPolicy gradePolicy = discountPolicyResolver.resolve(grade);
        int gradeDiscountedAmount = gradePolicy.calculate(order.getOriginalPrice());

        Optional<PaymentMethodDiscountPolicy> paymentMethodPolicy =
                paymentMethodDiscountPolicyResolver.resolve(request.paymentMethod());
        int finalAmount = paymentMethodPolicy
                .map(policy -> policy.calculate(gradeDiscountedAmount))
                .orElse(gradeDiscountedAmount);

        Payment payment = Payment.create(order, finalAmount, request.paymentMethod(), clock);
        paymentRepository.save(payment);

        discountHistoryService.saveGradeDiscountHistory(payment, grade, gradePolicy, order.getOriginalPrice());
        paymentMethodPolicy.ifPresent(policy ->
                discountHistoryService.savePaymentMethodDiscountHistory(payment, grade, policy, gradeDiscountedAmount)
        );

        return PaymentResponse.from(payment, order);
    }
}
