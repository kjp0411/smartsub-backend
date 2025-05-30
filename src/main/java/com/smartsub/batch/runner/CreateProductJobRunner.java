package com.smartsub.batch.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class CreateProductJobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job createProductJob;

    public CreateProductJobRunner(JobLauncher jobLauncher, Job createProductJob) {
        this.jobLauncher = jobLauncher;
        this.createProductJob = createProductJob;
    }

    @Override
    public void run(String... args) throws Exception {
        JobParameters params = new JobParametersBuilder()
            .addLong("timestamp", System.currentTimeMillis()) // 매번 실행되도록 시간 파라미터 추가
            .toJobParameters();

        jobLauncher.run(createProductJob, params);
    }
}
