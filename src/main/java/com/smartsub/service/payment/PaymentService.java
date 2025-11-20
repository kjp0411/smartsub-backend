package com.smartsub.service.payment;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.payment.Payment;
import com.smartsub.domain.payment.PaymentStatus;
import com.smartsub.domain.product.Product;
import com.smartsub.dto.payment.PaymentRequest;
import com.smartsub.dto.payment.PaymentResponse;
import com.smartsub.dto.slack.SlackMessage;
import com.smartsub.kafka.SlackKafkaProducer;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.payment.PaymentRepository;
import com.smartsub.repository.product.ProductRepository;
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

    public PaymentResponse createPayment(PaymentRequest request, Long memberId) {
        log.info("💳 PaymentRequest 수신: productId={}, quantity={}, amount={}, method={}",
            request.getProductId(), request.getQuantity(), request.getAmount(), request.getPaymentMethod());

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        int quantity = (request.getQuantity() == null ||  request.getQuantity() <= 0)
            ? 1
            : request.getQuantity();

        int amount;
        if (request.getAmount() == null || request.getAmount() <= 0) {
            // 프론트에서 amount 안 주면, 상품 가격 * 수량으로 계산
            amount = product.getPrice() * quantity;
        } else {
            amount = request.getAmount();
        }

        Payment payment = Payment.builder()
            .member(member)
            .product(product)
            .quantity(quantity)
            .amount(amount)
            .paymentMethod(request.getPaymentMethod())
            .status(PaymentStatus.PENDING)
            .build();

        // 여기서는 간단하게 금액이 0보다 크면 성공으로 처리
        if (amount > 0) {
            payment.markSuccess();

            try {
                // slack Kafka 알림 전송
                SlackMessage message = new SlackMessage(
                    member.getId().toString(),
                    member.getName() + "님, 결제가 완료되었습니다."
                );
                slackKafkaProducer.send(message);
            } catch (Exception e) {
                // ✅ 로컬 개발용: Kafka 장애는 로그만 찍고 결제는 계속 성공 처리
                log.warn("Slack Kafka 전송 실패 (무시하고 결제는 계속 진행): {}", e.getMessage());
            }
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
