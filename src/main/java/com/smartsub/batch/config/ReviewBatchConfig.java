package com.smartsub.batch.config;

import com.smartsub.batch.tasklet.GeneratePaymentDataTasklet;
import com.smartsub.batch.tasklet.GenerateReviewDataTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ReviewBatchConfig {

    private final GeneratePaymentDataTasklet generatePaymentDataTasklet;
    private final GenerateReviewDataTasklet generateReviewDataTasklet;

    @Bean
    public Job generatePaymentReviewJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("generatePaymentReviewJob", jobRepository)
            .start(generatePaymentStep(jobRepository, transactionManager))
            .next(generateReviewStep(jobRepository, transactionManager))
            .build();
    }

    @Bean
    public Step generatePaymentStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("generatePaymentStep", jobRepository)
            .tasklet(generatePaymentDataTasklet, transactionManager)
            .build();
    }

    @Bean
    public Step generateReviewStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("generateReviewStep", jobRepository)
            .tasklet(generateReviewDataTasklet, transactionManager)
            .build();
    }
}
