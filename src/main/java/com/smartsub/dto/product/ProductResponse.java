package com.smartsub.dto.product;

import com.smartsub.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {
    private Long id;            // 상품 ID
    private String name;        // 상품명
    private int price;          // 상품 가격
    private String category1;   // 상품 카테고리
    private String category2;   // 상품 카테고리
    private String category3;   // 상품 카테고리
    private String imageUrl;    // 상품 이미지 URL

    public static ProductResponse from(Product product) { // Product 객체를 ProductResponse로 변환하는 메서드
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory1(),
                product.getCategory2(),
                product.getCategory3(),
                product.getImageUrl()
        );
    }
}
