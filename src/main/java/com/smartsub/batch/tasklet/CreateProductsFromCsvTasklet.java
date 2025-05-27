package com.smartsub.batch.tasklet;

import com.opencsv.CSVReader;
import com.smartsub.domain.product.Product;
import com.smartsub.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class CreateProductsFromCsvTasklet implements Tasklet {

    private final ProductRepository productRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Path path = new ClassPathResource("product_test.csv").getFile().toPath();

        try (CSVReader reader = new CSVReader(new FileReader(path.toFile()))) {
            String[] fields;
            reader.readNext(); // 첫 줄 (헤더) 건너뛰기

            while ((fields = reader.readNext()) != null) {
                if (fields.length < 10) continue; // 최소 필드 개수 체크

                String rawPrice = fields[1].replaceAll("[^\\d]", "").trim();
                int price = rawPrice.isEmpty() ? 0 : Integer.parseInt(rawPrice);

                Product product = Product.builder()
                    .name(fields[0].trim())
                    .price(price)
                    .category(fields[9].trim())
                    .imageUrl(fields[4].trim())
                    .productCode(fields[6].trim())
                    .build();

                productRepository.save(product);
            }
        }
        return RepeatStatus.FINISHED;
    }
}
