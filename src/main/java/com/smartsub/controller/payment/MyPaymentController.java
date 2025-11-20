package com.smartsub.controller.payment;

import com.smartsub.dto.payment.PaymentResponse;
import com.smartsub.service.payment.PaymentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/me/payments")
@RequiredArgsConstructor
public class MyPaymentController {

    private final PaymentService paymentService;

    // 내 결제 내역 조회: GET /api/me/payments
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getMyPayments(
        @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(paymentService.findByMemberId(memberId));
    }
}
