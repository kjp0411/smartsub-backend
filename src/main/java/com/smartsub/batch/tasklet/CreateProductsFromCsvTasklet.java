package com.smartsub.batch.tasklet;

import com.smartsub.domain.product.Product;
import com.smartsub.repository.product.ProductRepository;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component // Spring Batch Tasklet으로 등록
public class CreateProductsFromCsvTasklet implements Tasklet {

    private final ProductRepository productRepository;

    public CreateProductsFromCsvTasklet(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Path path = new ClassPathResource("product_test.csv").getFile().toPath(); // CSV 파일 경로
        try (BufferedReader reader = Files.newBufferedReader(path)) { // BufferedReader로 파일 읽기
            String line; // 한 줄씩 읽기
            reader.readLine(); // 첫 번째 줄은 헤더이므로 건너뛰기
            while ((line = reader.readLine()) != null) { // 다음 줄 읽기
                String[] fields = line.split(","); // 쉼표로 구분된 필드로 나누기
                if (fields.length < 5) continue; // 필드가 5개 미만이면 건너뛰기

                Product product = Product.builder() // Product 객체 생성
                    .name(fields[0].trim())
                    .category(fields[11].trim())
                    .unit(fields[2].trim())
                    .imageUrl(fields[4].trim())
                    .productCode(fields[6].trim())
                    .build();

                productRepository.save(product);
            }
        }

        return RepeatStatus.FINISHED;
    }
}
