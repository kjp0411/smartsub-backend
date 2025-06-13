package com.smartsub.batch.config;

import com.smartsub.batch.tasklet.ProcessSubscriptionPaymentTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SubscriptionPaymentBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ProcessSubscriptionPaymentTasklet processSubscriptionPaymentTasklet;

    @Bean
    public Job processSubscriptionPaymentJob() {
        return new JobBuilder("processSubscriptionPaymentJob", jobRepository)
            .start(processSubscriptionPaymentStep())
            .build();
    }

    @Bean
    public Step processSubscriptionPaymentStep() {
        return new StepBuilder("processSubscriptionPaymentStep", jobRepository)
            .tasklet(processSubscriptionPaymentTasklet, transactionManager)
            .build();
    }
}
