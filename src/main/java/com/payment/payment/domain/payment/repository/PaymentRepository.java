package com.payment.payment.domain.payment.repository;

import com.payment.payment.domain.order.model.Order;
import com.payment.payment.domain.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    boolean existsByOrder(Order order);
}
