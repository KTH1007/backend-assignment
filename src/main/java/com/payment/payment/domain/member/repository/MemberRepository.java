package com.payment.payment.domain.member.repository;

import com.payment.payment.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
}
