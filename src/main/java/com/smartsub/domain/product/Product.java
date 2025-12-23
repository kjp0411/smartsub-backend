package com.smartsub.domain.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // 상품 ID

    @Column(nullable = false)
    private String name;            // 상품명

    @Column(nullable = false)
    private int price;              // 가격

    @Column(nullable = false)
    private String category1;       // ex: 식품

    @Column(nullable = false)
    private String category2;       // ex: 음료

    @Column(nullable = false)
    private String category3;       // ex: 생수

    @Column(nullable = false)
    private String productCode;     // 외부 상품ID

    @Column(nullable = false)
    private String imageUrl;        // 상품 이미지 URL

    public  void update(String name, String category1, String category2, String category3,  String imageUrl) {
        if (name != null) this.name = name;
        if (category1 != null) this.category1 = category1;
        if (category2 != null) this.category2 = category2;
        if (category3 != null) this.category3 = category3;
        if (imageUrl != null) this.imageUrl = imageUrl;
    }
}
