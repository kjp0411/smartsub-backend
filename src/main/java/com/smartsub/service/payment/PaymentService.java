package com.smartsub.service.payment;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.payment.Payment;
import com.smartsub.domain.payment.PaymentStatus;
import com.smartsub.dto.payment.PaymentRequest;
import com.smartsub.dto.payment.PaymentResponse;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.payment.PaymentRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 서비스 레이어를 나타내는 어노테이션
@RequiredArgsConstructor // 생성자 주입을 위한 어노테이션
@Transactional // 트랜잭션을 관리하기 위한 어노테이션
public class PaymentService {

    private final PaymentRepository paymentRepository; // 결제 정보를 저장하는 레포지토리
    private final MemberRepository memberRepository; // 회원 정보를 저장하는 레포지토리

    public PaymentResponse createPayment(PaymentRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다.")); // 회원 정보 조회

        // 결제 정보 생성
        Payment payment = Payment.builder()
            .member(member) // 회원 정보 설정
            .amount(request.getAmount()) // 결제 금액 설정
            .paymentMethod(request.getPaymentMethod()) // 결제 방법 설정
            .status(PaymentStatus.PENDING)
            .build();

        // 임시 로직: 금액이 0보다 크면 결제 성공, 아니면 실패
        if (request.getAmount() > 0) {
            payment.markSuccess();
        } else {
            payment.markFailed();
        }

        Payment saved = paymentRepository.save(payment); // 결제 정보 저장
        return PaymentResponse.from(saved); // 저장된 결제 정보를 PaymentResponse로 변환하여 반환
    }

    // 결제 정보 단건 조회
    public PaymentResponse findById(Long id) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 결제 내역이 없습니다.")); // 결제 정보 조회
        return PaymentResponse.from(payment); // 결제 정보를 PaymentResponse로 변환하여 반
    }

    // 회원 ID 기준 목록 조회
    public List<PaymentResponse> findByMemberId(Long memberId) {
        return paymentRepository.findByMemberId(memberId).stream()
            .map(PaymentResponse::from) // 결제 정보를 PaymentResponse로 변환
            .collect(Collectors.toList()); // 리스트로 변환
    }

    // 전체 결제 목록 조회
    public List<PaymentResponse> findAll() {
        return paymentRepository.findAll().stream()
            .map(PaymentResponse::from)
            .collect(Collectors.toList());
    }
}
