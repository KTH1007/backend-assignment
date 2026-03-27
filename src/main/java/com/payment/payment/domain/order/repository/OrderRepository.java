package com.payment.payment.domain.order.repository;

import com.payment.payment.domain.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
