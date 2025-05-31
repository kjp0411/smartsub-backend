package com.smartsub.batch.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeneratePaymentReviewJobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job generatePaymentReviewJob;  // ReviewBatchConfig에서 등록한 Job Bean

    @Override
    public void run(String... args) throws Exception {
        JobParameters params = new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())  // 매번 실행되도록 시간 파라미터 추가
            .toJobParameters();

        jobLauncher.run(generatePaymentReviewJob, params);
    }
}
