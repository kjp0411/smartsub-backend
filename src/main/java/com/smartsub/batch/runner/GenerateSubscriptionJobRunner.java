package com.smartsub.batch.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@RequiredArgsConstructor
public class GenerateSubscriptionJobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job generateSubscriptionJob;

    @Override
    public void run(String... args) throws Exception {
        log.info("🚀 정기 구독 더미 생성 배치 실행 시작");

        JobParameters jobParameters = new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis()) // 매번 고유 파라미터
            .toJobParameters();

        jobLauncher.run(generateSubscriptionJob, jobParameters);

        log.info("✅ 정기 구독 더미 생성 배치 실행 완료");
    }
}
