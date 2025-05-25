package com.smartsub.batch.config;

import com.smartsub.batch.tasklet.CreateProductsFromCsvTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Spring Batch 5 방식으로 작성된 Product 등록 배치 구성 클래스
 */
@Configuration
@RequiredArgsConstructor
public class ProductBatchConfig {

    private final JobRepository jobRepository; // Job 메타데이터 저장 및 관리용
    private final PlatformTransactionManager transactionManager; // 트랜잭션 관리
    private final CreateProductsFromCsvTasklet createProductsFromCsvTasklet; // CSV 기반 상품 등록 Tasklet

    /**
     * 상품 등록 배치 작업(Job) 정의
     */
    @Bean
    public Job createProductJob() {
        return new JobBuilder("createProductJob", jobRepository)
            .start(createProductStep()) // Step부터 시작
            .build(); // Job 빌드
    }

    /**
     * 상품 등록 Step 정의
     */
    @Bean
    public Step createProductStep() {
        return new StepBuilder("createProductStep", jobRepository)
            .tasklet(createProductsFromCsvTasklet, transactionManager) // Tasklet 기반 Step 구성
            .build();
    }
}
