package com.payment.payment.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),

    // Order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),

    // Payment
    ALREADY_PAID(HttpStatus.CONFLICT, "이미 결제된 주문입니다."),
    DISCOUNT_POLICY_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "적용 가능한 할인 정책을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}