package com.payment.payment.domain.discount.history.repository;

import com.payment.payment.domain.discount.history.model.DiscountHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DiscountHistoryRepository extends JpaRepository<DiscountHistory, UUID> {
}
