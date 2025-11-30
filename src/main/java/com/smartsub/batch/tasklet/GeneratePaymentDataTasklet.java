package com.smartsub.batch.tasklet;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.payment.Payment;
import com.smartsub.domain.product.Product;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.payment.PaymentRepository;
import com.smartsub.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeneratePaymentDataTasklet implements Tasklet {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    private static final String[] METHODS = {"CARD", "KAKAO_PAY", "ACCOUNT_TRANSFER"};
    private final Random random = new Random();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        List<Member> members = memberRepository.findAll();
        List<Product> products = productRepository.findAll();

        if (members.isEmpty() || products.isEmpty()) {
            log.warn("❗ 결제 데이터를 생성할 수 없습니다. 회원 또는 상품 리스트가 비어 있습니다.");
            return RepeatStatus.FINISHED;
        }

        int count = 0;

        // 회원 × 상품 조합에 대해 여러 회 결제 생성
        for (int i = 0; i < 30; i++) {
            Member member = members.get(random.nextInt(members.size()));
            Product product = products.get(random.nextInt(products.size()));

            int repeat = 2 + random.nextInt(2); // 2~3회 결제 생성

            for (int j = 0; j < repeat; j++) {
                LocalDateTime paidAt = LocalDateTime.now().minusDays(random.nextInt(30));

                Payment payment = Payment.builder()
                    .member(member)
                    .product(product)
                    .amount(product.getPrice())
                    .paymentMethod(METHODS[random.nextInt(METHODS.length)])
                    .paidAt(paidAt)
                    .build();

                paymentRepository.save(payment);
                count++;
            }
        }

        return RepeatStatus.FINISHED;
    }
}
