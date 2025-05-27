package com.smartsub.service.payment;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.payment.Payment;
import com.smartsub.domain.payment.PaymentStatus;
import com.smartsub.domain.product.Product;
import com.smartsub.dto.payment.PaymentRequest;
import com.smartsub.dto.payment.PaymentResponse;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.payment.PaymentRepository;
import com.smartsub.repository.product.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // ✅ memberId를 별도로 받는 버전
    public PaymentResponse createPayment(PaymentRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        Payment payment = Payment.builder()
            .member(member)
            .product(product)
            .amount(request.getAmount())
            .paymentMethod(request.getPaymentMethod())
            .status(PaymentStatus.PENDING)
            .build();

        if (request.getAmount() > 0) {
            payment.markSuccess();
        } else {
            payment.markFailed();
        }

        Payment saved = paymentRepository.save(payment);
        return PaymentResponse.from(saved);
    }

    public PaymentResponse findById(Long id) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 결제 내역이 없습니다."));
        return PaymentResponse.from(payment);
    }

    public List<PaymentResponse> findByMemberId(Long memberId) {
        return paymentRepository.findByMemberId(memberId).stream()
            .map(PaymentResponse::from)
            .collect(Collectors.toList());
    }

    public List<PaymentResponse> findAll() {
        return paymentRepository.findAll().stream()
            .map(PaymentResponse::from)
            .collect(Collectors.toList());
    }
}
