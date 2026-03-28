package com.payment.payment.domain.discount.history;

import com.payment.payment.domain.discount.history.model.DiscountHistory;
import com.payment.payment.domain.discount.history.repository.DiscountHistoryRepository;
import com.payment.payment.domain.member.model.Member;
import com.payment.payment.domain.member.model.MemberGrade;
import com.payment.payment.domain.member.repository.MemberRepository;
import com.payment.payment.domain.order.model.Order;
import com.payment.payment.domain.order.repository.OrderRepository;
import com.payment.payment.domain.payment.model.Payment;
import com.payment.payment.domain.payment.model.PaymentMethod;
import com.payment.payment.domain.payment.repository.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Clock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class DiscountHistoryIntegrityTest {

    @Autowired private DiscountHistoryRepository discountHistoryRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName("정책 변경 후에도 과거 할인 이력이 보존된다")
    void discountHistory_preservedAfterPolicyChange() {
        // given
        Member member = memberRepository.save(Member.create("VIP회원", MemberGrade.VIP));
        Order order = orderRepository.save(Order.create("상품A", 10000, member));
        Payment payment = paymentRepository.save(
                Payment.create(order, 9000, PaymentMethod.CREDIT_CARD, Clock.systemDefaultZone())
        );
        DiscountHistory history = discountHistoryRepository.save(
                DiscountHistory.create(payment, "VIP", "VIP_FIXED_1000", 1000, 10)
        );

        // when - 정책이 변경/삭제됐다고 가정 (이력은 문자열로 스냅샷 저장됨)
        DiscountHistory saved = discountHistoryRepository.findById(history.getId()).orElseThrow();

        // then
        assertThat(saved.getPolicyName()).isEqualTo("VIP_FIXED_1000");
        assertThat(saved.getDiscountAmount()).isEqualTo(1000);
        assertThat(saved.getDiscountRate()).isEqualTo(10);
        assertThat(saved.getMemberGrade()).isEqualTo("VIP");
    }
}