package com.payment.payment.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    PAYMENT_SUCCESS(HttpStatus.CREATED, "결제가 완료되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
