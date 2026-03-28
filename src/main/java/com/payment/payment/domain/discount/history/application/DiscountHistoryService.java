package com.payment.payment.domain.discount.history.application;

import com.payment.payment.domain.discount.history.model.DiscountHistory;
import com.payment.payment.domain.discount.history.repository.DiscountHistoryRepository;
import com.payment.payment.domain.discount.model.DiscountPolicy;
import com.payment.payment.domain.member.model.MemberGrade;
import com.payment.payment.domain.payment.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiscountHistoryService {

    private final DiscountHistoryRepository discountHistoryRepository;

    public void saveGradeDiscountHistory(Payment payment, MemberGrade memberGrade, DiscountPolicy policy, int originalPrice) {
        int discountAmount = originalPrice - policy.calculate(originalPrice);
        int discountRate = policy.getDiscountRate(originalPrice);

        discountHistoryRepository.save(DiscountHistory.create(
                payment,
                memberGrade.name(),
                policy.getPolicyName(),
                discountAmount,
                discountRate
        ));
    }
}
