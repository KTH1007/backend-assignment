package com.payment.payment.domain.member.model;

import com.payment.payment.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberGrade grade;

    @Builder
    private Member(String name, MemberGrade grade) {
        this.name = name;
        this.grade = grade;
    }

    public static Member create(String name, MemberGrade grade) {
        return Member.builder()
                .name(name)
                .grade(grade)
                .build();
    }
}
