package com.smartsub.controller.payment;

import com.smartsub.dto.payment.PaymentRequest;
import com.smartsub.dto.payment.PaymentResponse;
import com.smartsub.service.payment.PaymentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController // RestController 어노테이션을 사용하여 RESTful API를 제공하는 컨트롤러로 설정
@RequestMapping("/api/payments") // API의 기본 URL 경로를 설정
@RequiredArgsConstructor // 생성자 주입을 위한 어노테이션
public class PaymentController {

    private final PaymentService paymentService; // 결제 정보를 처리하는 서비스

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
        @RequestBody PaymentRequest request,
        @AuthenticationPrincipal Object principal // principal은 JWT 인증 필터에서 설정한 값
    ) {
        Long memberId;
        if (principal instanceof String) {
            memberId = Long.parseLong((String) principal); // 예: "25"
        } else if (principal instanceof Long) {
            memberId = (Long) principal;
        } else {
            throw new IllegalArgumentException("회원 정보 확인 불가");
        }

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
