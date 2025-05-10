package com.smartsub.controller.payment;

import com.smartsub.dto.payment.PaymentRequest;
import com.smartsub.dto.payment.PaymentResponse;
import com.smartsub.service.payment.PaymentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // RestController 어노테이션을 사용하여 RESTful API를 제공하는 컨트롤러로 설정
@RequestMapping("/api/payments") // API의 기본 URL 경로를 설정
@RequiredArgsConstructor // 생성자 주입을 위한 어노테이션
public class PaymentController {

    private final PaymentService paymentService; // 결제 정보를 처리하는 서비스

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request); // 결제 등록 서비스 호출
        return ResponseEntity.ok(response); // 등록 성공 시 200 응답 반환
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.findById(id)); // 결제 ID로 결제 정보 조회
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(paymentService.findByMemberId(memberId)); // 회원 ID로 결제 정보 조회
    }
}
