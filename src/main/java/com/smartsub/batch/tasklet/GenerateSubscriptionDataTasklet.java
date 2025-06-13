package com.smartsub.batch.tasklet;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.product.Product;
import com.smartsub.domain.subscription.Subscription;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.product.ProductRepository;
import com.smartsub.repository.subscription.SubscriptionRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenerateSubscriptionDataTasklet implements Tasklet {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final SubscriptionRepository subscriptionRepository;

    private final Random random = new Random();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<Member> members = memberRepository.findAll();
        List<Product> products = productRepository.findAll();

        if (members.isEmpty() || products.isEmpty()) {
            System.out.println("⚠️ 회원 또는 상품 데이터가 비어있습니다.");
            return RepeatStatus.FINISHED;
        }

        for (int i = 0; i < 10; i++) {
            Member member = members.get(random.nextInt(members.size()));
            Product product = products.get(random.nextInt(products.size()));

            Subscription subscription = Subscription.builder()
                .member(member)
                .product(product)
                .cycleDays(List.of(7, 14, 30).get(random.nextInt(3))) // 7, 14, 30일 중 랜덤 선택
                .lastPaidDate(LocalDate.now().minusDays(random.nextInt(30))) // 최근 결제일
                .active(true) // 활성화 상태
                .preferredTime(LocalTime.of(11 + random.nextInt(4), 0)) // 선호 결제 시간
                .build();

            subscriptionRepository.save(subscription);
        }
        return RepeatStatus.FINISHED;
    }
}
