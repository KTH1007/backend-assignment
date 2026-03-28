package com.payment.payment.domain.payment.application;

import com.payment.payment.domain.discount.history.application.DiscountHistoryService;
import com.payment.payment.domain.discount.policy.NormalDiscountPolicy;
import com.payment.payment.domain.discount.policy.VipDiscountPolicy;
import com.payment.payment.domain.discount.policy.VvipDiscountPolicy;
import com.payment.payment.domain.discount.resolver.DiscountPolicyResolver;
import com.payment.payment.domain.member.model.Member;
import com.payment.payment.domain.member.model.MemberGrade;
import com.payment.payment.domain.order.model.Order;
import com.payment.payment.domain.order.repository.OrderRepository;
import com.payment.payment.domain.payment.dto.request.PaymentRequest;
import com.payment.payment.domain.payment.dto.response.PaymentResponse;
import com.payment.payment.domain.payment.model.PaymentMethod;
import com.payment.payment.domain.discount.policy.PointDiscountPolicy;
import com.payment.payment.domain.payment.repository.PaymentRepository;
import com.payment.payment.global.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private PaymentRepository paymentRepository;
    @Mock private DiscountHistoryService discountHistoryService;
    @InjectMocks private PaymentService paymentService;

    private final Clock fixedClock = Clock.fixed(
            Instant.parse("2026-01-01T00:00:00Z"), ZoneId.systemDefault()
    );

    private DiscountPolicyResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new DiscountPolicyResolver(List.of(
                new NormalDiscountPolicy(),
                new VipDiscountPolicy(),
                new VvipDiscountPolicy()
        ));
        paymentService = new PaymentService(orderRepository, paymentRepository, resolver, discountHistoryService,
                new PointDiscountPolicy(), fixedClock);
    }

    @Test
    @DisplayName("NORMAL 등급 회원은 할인 없이 결제된다.")
    void pay_normalMember_noDiscount() {
        Member member = Member.create("일반회원", MemberGrade.NORMAL);
        Order order = Order.create("상품A", 10000, member);
        UUID orderId = UUID.randomUUID();

        given(orderRepository.findByIdWithLock(any())).willReturn(Optional.of(order));
        given(paymentRepository.existsByOrder(order)).willReturn(false);

        PaymentResponse response = paymentService.pay(new PaymentRequest(orderId, PaymentMethod.CREDIT_CARD));

        assertThat(response.finalAmount()).isEqualTo(10000);
    }

    @Test
    @DisplayName("VIP 등급 회원은 1,000원 할인 후 결제된다")
    void pay_vipMember_fixedDiscount() {
        Member member = Member.create("VIP회원", MemberGrade.VIP);
        Order order = Order.create("상품A", 10000, member);
        UUID orderId = UUID.randomUUID();

        given(orderRepository.findByIdWithLock(any())).willReturn(Optional.of(order));
        given(paymentRepository.existsByOrder(order)).willReturn(false);

        PaymentResponse response = paymentService.pay(new PaymentRequest(orderId, PaymentMethod.CREDIT_CARD));

        assertThat(response.finalAmount()).isEqualTo(9000);
    }

    @Test
    @DisplayName("VVIP 등급 회원은 10% 할인 후 결제된다")
    void pay_vvipMember_rateDiscount() {
        Member member = Member.create("VVIP회원", MemberGrade.VVIP);
        Order order = Order.create("상품A", 10000, member);
        UUID orderId = UUID.randomUUID();

        given(orderRepository.findByIdWithLock(any())).willReturn(Optional.of(order));
        given(paymentRepository.existsByOrder(order)).willReturn(false);

        PaymentResponse response = paymentService.pay(new PaymentRequest(orderId, PaymentMethod.CREDIT_CARD));

        assertThat(response.finalAmount()).isEqualTo(9000);
    }

    @Test
    @DisplayName("이미 결제된 주문은 예외가 발생한다")
    void pay_alreadyPaid_throwsException() {
        Member member = Member.create("VIP회원", MemberGrade.VIP);
        Order order = Order.create("상품A", 10000, member);
        UUID orderId = UUID.randomUUID();

        given(orderRepository.findByIdWithLock(any())).willReturn(Optional.of(order));
        given(paymentRepository.existsByOrder(order)).willReturn(true);

        assertThatThrownBy(() -> paymentService.pay(new PaymentRequest(orderId, PaymentMethod.CREDIT_CARD)))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("결제 완료 시 할인 이력이 저장된다")
    void pay_savesDiscountHistory() {
        Member member = Member.create("VIP회원", MemberGrade.VIP);
        Order order = Order.create("상품A", 10000, member);

        given(orderRepository.findByIdWithLock(any())).willReturn(Optional.of(order));
        given(paymentRepository.existsByOrder(order)).willReturn(false);

        paymentService.pay(new PaymentRequest(UUID.randomUUID(), PaymentMethod.CREDIT_CARD));

        verify(discountHistoryService).saveGradeDiscountHistory(
                any(), eq(MemberGrade.VIP), any(), eq(10000)
        );
    }

    @Test
    @DisplayName("포인트 결제 시 등급 할인 후 추가 5% 할인이 적용된다")
    void pay_pointPayment_additionalDiscount() {
        Member member = Member.create("VIP회원", MemberGrade.VIP);
        Order order = Order.create("상품A", 10000, member);

        given(orderRepository.findByIdWithLock(any())).willReturn(Optional.of(order));
        given(paymentRepository.existsByOrder(order)).willReturn(false);

        PaymentResponse response = paymentService.pay(new PaymentRequest(UUID.randomUUID(), PaymentMethod.POINT));

        // VIP 1000원 할인 → 9000원, 포인트 5% 추가 할인 → 8550원
        assertThat(response.finalAmount()).isEqualTo(8550);
    }

    @Test
    @DisplayName("신용카드 결제 시 포인트 추가 할인이 적용되지 않는다")
    void pay_creditCardPayment_noAdditionalDiscount() {
        Member member = Member.create("VIP회원", MemberGrade.VIP);
        Order order = Order.create("상품A", 10000, member);

        given(orderRepository.findByIdWithLock(any())).willReturn(Optional.of(order));
        given(paymentRepository.existsByOrder(order)).willReturn(false);

        PaymentResponse response = paymentService.pay(new PaymentRequest(UUID.randomUUID(), PaymentMethod.CREDIT_CARD));

        assertThat(response.finalAmount()).isEqualTo(9000);
    }
}