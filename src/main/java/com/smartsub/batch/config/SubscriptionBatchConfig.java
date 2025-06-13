package com.smartsub.batch.config;

import com.smartsub.batch.tasklet.GenerateSubscriptionDataTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SubscriptionBatchConfig {

    private final GenerateSubscriptionDataTasklet generateSubscriptionDataTasklet;

    @Bean
    public Job generateSubscriptionJob(JobRepository jobRepository, Step generateSubscriptionStep) {
        return new JobBuilder("generateSubscriptionJob", jobRepository)
            .start(generateSubscriptionStep)
            .build();
    }

    @Bean
    public Step generateSubscriptionStep(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {
        return new StepBuilder("generateSubscriptionStep", jobRepository)
            .tasklet(generateSubscriptionDataTasklet, transactionManager)
            .build();
    }
}
