package com.smartsub.batch.tasklet;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.payment.Payment;
import com.smartsub.domain.payment.PaymentStatus;
import com.smartsub.domain.product.Product;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.payment.PaymentRepository;
import com.smartsub.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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

        for (int i = 0; i < 50; i++) {
            Member member = members.get(random.nextInt(members.size()));
            Product product = products.get(random.nextInt(products.size()));

            Payment payment = Payment.builder()
                .member(member)
                .product(product)
                .amount(product.getPrice())
                .paymentMethod(METHODS[random.nextInt(METHODS.length)])
                .status(PaymentStatus.SUCCESS)
                .paidAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                .build();

            paymentRepository.save(payment);
        }

        return RepeatStatus.FINISHED;
    }
}
