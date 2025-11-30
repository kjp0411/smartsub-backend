//package com.smartsub.batch.tasklet;
//
//import com.smartsub.domain.payment.Payment;
//import com.smartsub.domain.subscription.Subscription;
//import com.smartsub.repository.payment.PaymentRepository;
//import com.smartsub.repository.subscription.SubscriptionRepository;
//import com.smartsub.service.slack.SlackWebhookService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.StepContribution;
//import org.springframework.batch.core.scope.context.ChunkContext;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class ProcessSubscriptionPaymentTasklet implements Tasklet {
//
//    private final SubscriptionRepository subscriptionRepository;
//    private final PaymentRepository paymentRepository;
//    private final SlackWebhookService slackNotificationService; // 추가
//
//    @Override
//    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
//        LocalDate today = LocalDate.now();
//
//        List<Subscription> dueSubscriptions = subscriptionRepository.findAll()
//            .stream()
//            .filter(subscription -> subscription.isPaymentDueToday(today))
//            .toList();
//
//        if (dueSubscriptions.isEmpty()) {
//            log.info("오늘 결제 대상 구독이 없습니다.");
//            return RepeatStatus.FINISHED;
//        }
//
//        List<Subscription> processed = new ArrayList<>();
//
//        for (Subscription subscription : dueSubscriptions) {
//            Payment payment = Payment.builder()
//                .member(subscription.getMember())
//                .product(subscription.getProduct())
//                .amount(subscription.getProduct().getPrice())
//                .paymentMethod("CARD")
//                .paidAt(today.atTime(subscription.getPreferredTime()))
//                .build();
//
//            paymentRepository.save(payment);
//            subscription.markPaid(today);
//            subscriptionRepository.save(subscription);
//            processed.add(subscription); // 처리된 목록 추가
//        }
//
//
//        // Slack 알림 전송
//        slackNotificationService.sendSubscriptionPaymentSummary(processed);
//
//        return RepeatStatus.FINISHED;
//    }
//}
