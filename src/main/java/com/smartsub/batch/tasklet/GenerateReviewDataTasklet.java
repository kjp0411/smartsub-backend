package com.smartsub.batch.tasklet;

import com.smartsub.domain.payment.Payment;
import com.smartsub.domain.review.Review;
import com.smartsub.repository.payment.PaymentRepository;
import com.smartsub.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@RequiredArgsConstructor
public class GenerateReviewDataTasklet implements Tasklet {

    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;

    private final Random random = new Random();

    private Map<String, List<String>> reviewMap;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        if (reviewMap == null) {
            reviewMap = loadReviewSentences();
        }

        List<Payment> payments = paymentRepository.findAll();

        for (Payment payment : payments) {
            if (reviewRepository.existsByPayment(payment)) continue; // 중복 방지

            // 랜덤하게 감정 선택
            String[] emotionOptions = {"positive", "neutral", "negative"};
            String emotion = emotionOptions[random.nextInt(emotionOptions.length)];
            List<String> candidates = reviewMap.getOrDefault(emotion, Collections.emptyList());

            if (candidates.isEmpty()) {
                System.out.printf("⚠️ 리뷰 문장이 비어 있음: [%s]%n", emotion);
                continue;
            }

            String content = candidates.get(random.nextInt(candidates.size()));

            // 감정에 따라 별점 지정
            int rating = switch (emotion) {
                case "positive" -> 4 + random.nextInt(2); // 4 ~ 5
                case "neutral" -> 3;
                case "negative" -> 1 + random.nextInt(2); // 1 ~ 2
                default -> 3;
            };

            Review review = Review.builder()
                .member(payment.getMember())
                .product(payment.getProduct())
                .payment(payment)
                .content(content)
                .rating(rating)
                .build();

            reviewRepository.save(review);
        }

        return RepeatStatus.FINISHED;
    }

    private Map<String, List<String>> loadReviewSentences() throws Exception {
        Map<String, List<String>> map = new HashMap<>();
        map.put("positive", new ArrayList<>());
        map.put("neutral", new ArrayList<>());
        map.put("negative", new ArrayList<>());

        ClassPathResource resource = new ClassPathResource("review_sentences.csv");
        String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        String[] lines = content.split("\n");

        for (String line : lines) {
            String[] tokens = line.trim().split(",", 2);
            if (tokens.length < 2) continue;

            String emotion = tokens[0].trim().toLowerCase();
            String sentence = tokens[1].trim();

            switch (emotion) {
                case "positive" -> map.get("positive").add(sentence);
                case "neutral" -> map.get("neutral").add(sentence);
                case "negative" -> map.get("negative").add(sentence);
            }
        }

        return map;
    }
}
