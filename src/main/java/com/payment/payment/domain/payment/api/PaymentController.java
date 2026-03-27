package com.payment.payment.domain.payment.api;

import com.payment.payment.domain.payment.application.PaymentService;
import com.payment.payment.domain.payment.dto.request.PaymentRequest;
import com.payment.payment.domain.payment.dto.response.PaymentResponse;
import com.payment.payment.global.response.ApiResponse;
import com.payment.payment.global.response.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> pay(@Valid @RequestBody PaymentRequest request) {
        return ApiResponse.success(SuccessCode.PAYMENT_SUCCESS, paymentService.pay(request));
    }
}
