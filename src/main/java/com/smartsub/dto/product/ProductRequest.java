package com.smartsub.dto.product;

import com.smartsub.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductRequest {

    private String name;        // 상품명
    private String productCode; // 상품코드
    private String category1;   // 카테고리
    private String category2;   // 카테고리
    private String category3;   // 카테고리
    private String imageUrl;    // 상품 이미지 URL

    public Product toEntity() { // ProductRequest를 Product 엔티티로 변환하는 메서드
        return Product.builder()
                .name(name)
                .productCode(productCode)
                .category1(category1)
                .category2(category2)
                .category3(category3)
                .imageUrl(imageUrl)
                .build();
    }
}
