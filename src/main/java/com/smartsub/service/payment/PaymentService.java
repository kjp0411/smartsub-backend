package com.smartsub.service.payment;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.payment.Payment;
import com.smartsub.domain.product.Product;
import com.smartsub.dto.payment.PaymentRequest;
import com.smartsub.dto.payment.PaymentResponse;
import com.smartsub.dto.slack.SlackMessage;
import com.smartsub.kafka.SlackKafkaProducer;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.payment.PaymentRepository;
import com.smartsub.repository.product.ProductRepository;
import com.smartsub.repository.slack.SlackUserRepository;
import com.smartsub.service.slack.SlackDmService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final SlackKafkaProducer slackKafkaProducer;
    private final SlackUserRepository slackUserRepository;
    private final SlackDmService slackDmService;

    public PaymentResponse createPayment(PaymentRequest request, Long memberId) {

        // 회원 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        // 상품 조회
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // 수량 기본값 처리
        int quantity = (request.getQuantity() == null || request.getQuantity() <= 0)
            ? 1
            : request.getQuantity();

        // 결제 금액 계산
        int amount = product.getPrice() * quantity;
        if (request.getAmount() != null && request.getAmount() > 0) {
            amount = request.getAmount(); // (쿠폰 등) 외부에서 조정된 금액
        }

        // 결제 엔티티 생성
        Payment payment = Payment.builder()
            .member(member)
            .product(product)
            .quantity(quantity)
            .amount(amount)
            .paymentMethod(request.getPaymentMethod())
            .build();

        // 결제 저장
        Payment saved = paymentRepository.save(payment);

        // Slack 알림 (동기 처리)
        try {
            String message =
                member.getName() + "님, 결제가 완료되었습니다.\n\n"
                    + "상품명: " + product.getName() + "\n"
                    + "수량: " + quantity + "개\n"
                    + "결제 금액: " + amount + "원";

            slackDmService.sendByMemberId(memberId, message);

            // Kafka 비동기 전송은 다음 단계에서 다시 활성화
            // SlackMessage kafkaMessage = new SlackMessage(memberId, message);
            // slackKafkaProducer.send(kafkaMessage);

        } catch (Exception e) {
            // 결제는 성공해야 하므로 알림 실패는 로그만
            log.warn("Slack DM 전송 실패 (결제는 정상 처리됨): {}", e.getMessage());
        }

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