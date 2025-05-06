package com.smartsub.repository.product;

import com.smartsub.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository를 상속받아 기본 CRUD 메서드를 사용할 수 있음
    // 추가적인 쿼리 메서드는 필요에 따라 정의할 수 있음
    boolean existsByProductCode(String productCode); // 상품 코드 중복 검사
}
