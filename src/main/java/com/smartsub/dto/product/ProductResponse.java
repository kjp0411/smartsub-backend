package com.smartsub.dto.product;

import com.smartsub.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter // Getter 어노테이션을 사용하여 모든 필드에 대한 getter 메서드를 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 자동 생성
public class ProductResponse {
    private Long id; // 상품 ID
    private String name; // 상품명
    private int price; // 상품 가격
    private String category; // 카테고리
    private String unit; // ex: 1개, 1박스 등
    private String imageUrl; // 상품 이미지 URL

    public static ProductResponse from(Product product) { // Product 객체를 ProductResponse로 변환하는 메서드
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory(),
                product.getUnit(),
                product.getImageUrl()
        );
    }
}
