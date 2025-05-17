package com.smartsub.batch.config;

import com.smartsub.batch.tasklet.CreateDummyMembersTasklet;
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
public class MemberBatchConfig {

    private final CreateDummyMembersTasklet createDummyMembersTasklet;

    @Bean
    public Job createDummyMemberJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("createDummyMemberJob", jobRepository)
            .start(createDummyMemberStep(jobRepository, transactionManager))
            .build();
    }

    @Bean
    public Step createDummyMemberStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("createDummyMemberStep", jobRepository)
            .tasklet(createDummyMembersTasklet, transactionManager)
            .allowStartIfComplete(true) // 이미 완료된 작업을 다시 시작할 수 있도록 설정
            .build();
    }
}
