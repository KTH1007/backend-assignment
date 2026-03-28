package com.payment.payment.domain.payment.application;

import com.payment.payment.domain.discount.history.application.DiscountHistoryService;
import com.payment.payment.domain.discount.model.DiscountPolicy;
import com.payment.payment.domain.discount.resolver.DiscountPolicyResolver;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final DiscountPolicyResolver discountPolicyResolver;
    private final DiscountHistoryService discountHistoryService;
    private final Clock clock;

    @Transactional
    public PaymentResponse pay(PaymentRequest request) {
        Order order = orderRepository.findByIdWithLock(request.orderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        if (paymentRepository.existsByOrder(order)) {
            throw new BusinessException(ErrorCode.ALREADY_PAID);
        }

        MemberGrade grade = order.getMember().getGrade();
        DiscountPolicy policy = discountPolicyResolver.resolve(grade);
        int finalAmount = policy.calculate(order.getOriginalPrice());

        Payment payment = Payment.create(order, finalAmount, request.paymentMethod(), clock);
        paymentRepository.save(payment);

        discountHistoryService.saveGradeDiscountHistory(payment, grade, policy, order.getOriginalPrice());

        return PaymentResponse.from(payment, order);
    }
}
