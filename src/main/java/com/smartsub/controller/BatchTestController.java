package com.smartsub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/batch")
public class BatchTestController {

    private final JobLauncher jobLauncher;
    private final Job createDummyMemberJob;
    private final Job createProductJob;
    private final Job generatePaymentReviewJob;
    private final Job generateSubscriptionJob;
    private final Job processSubscriptionPaymentJob;

    @PostMapping("/generate-dummy-members")
    public String runDummyMemberJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .toJobParameters();
        jobLauncher.run(createDummyMemberJob, params);
        return "✅ 더미 회원 생성 완료";
    }

    @PostMapping("/generate-dummy-products")
    public String runProductJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .toJobParameters();
        jobLauncher.run(createProductJob, params);
        return "✅ 더미 상품 등록 완료";
    }

    @PostMapping("/generate-payments-reviews")
    public String runPaymentReviewJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .toJobParameters();
        jobLauncher.run(generatePaymentReviewJob, params);
        return "✅ 결제 + 리뷰 생성 완료";
    }

    @PostMapping("/generate-subscriptions")
    public String runSubscriptionJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .toJobParameters();
        jobLauncher.run(generateSubscriptionJob, params);
        return "✅ 정기 구독 생성 완료";
    }

    @PostMapping("/process-subscription-payments")
    public String runSubscriptionPaymentJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .toJobParameters();
        jobLauncher.run(processSubscriptionPaymentJob, params);
        return "✅ 정기 결제 실행 완료";
    }

}
