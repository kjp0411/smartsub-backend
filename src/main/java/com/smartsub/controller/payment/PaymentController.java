package com.smartsub.controller.payment;

import com.smartsub.dto.payment.PaymentRequest;
import com.smartsub.dto.payment.PaymentResponse;
import com.smartsub.service.payment.PaymentService;
import com.smartsub.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtTokenProvider jwtTokenProvider;

    // 결제 생성: POST /api/payments
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
        @RequestBody PaymentRequest request,
        HttpServletRequest httpRequest
    ) {
        String token = jwtTokenProvider.resolveToken(httpRequest);
        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);

        PaymentResponse response = paymentService.createPayment(request, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.findById(id)); // 결제 ID로 결제 정보 조회
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(paymentService.findByMemberId(memberId)); // 회원 ID로 결제 정보 조회
    }

    // 전체 결제 목록 조회 (프론트에서 사용)
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.findAll());
    }


}
