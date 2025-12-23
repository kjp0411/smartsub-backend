package com.smartsub.service.product;

import com.smartsub.domain.product.Product;
import com.smartsub.dto.product.ProductResponse;
import com.smartsub.dto.product.ProductUpdateRequest;
import com.smartsub.repository.product.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository; // 상품 정보를 저장하는 레포지토리

    public Long createProduct(Product product) {
        validateDuplicateProductCode(product.getProductCode()); // 중복 검사 추가
        return productRepository.save(product).getId(); // 저장 후 생성된 ID 반환
    }

    public List<ProductResponse> findAll() { // 모든 상품 정보를 조회하는 메서드
        return productRepository.findAll()
            .stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }

    public ProductResponse findById(Long id) { // 상품 ID로 상품 정보를 조회하는 메서드
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        return ProductResponse.from(product);
    }

    public void updateProduct(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        product.update(
            request.getName(),
            request.getCategory1(),
            request.getCategory2(),
            request.getCategory3(),
            request.getImageUrl()
        );
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        productRepository.delete(product);
    }

    /**
     * 상품 코드 중복 검사
     * - 동일한 productCode가 존재할 경우 예외 발생
     */
    public void validateDuplicateProductCode(String code) {
        if (productRepository.existsByProductCode(code)) {
            throw new IllegalStateException("이미 존재하는 상품 코드입니다.");
        }
    }
}
