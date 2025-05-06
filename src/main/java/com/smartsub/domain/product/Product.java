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

@Entity // JPA 엔티티로 지정
@Getter // Getter 어노테이션을 사용하여 모든 필드에 대한 getter 메서드를 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 protected로 설정하여 외부에서 직접 인스턴스를 생성하지 못하도록 함
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 자동 생성
@Builder // Builder 패턴을 사용하여 객체를 생성할 수 있도록 함
public class Product {

    @Id // JPA에서 기본 키로 사용할 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임
    private Long id; // 상품 ID

    @Column(nullable = false) // null을 허용하지 않음
    private String name; // 상품명

    @Column(nullable = false) // null을 허용하지 않음
    private String category; // 카테고리

    @Column(nullable = false) // null을 허용하지 않음
    private String productCode; // 외부 크롤링 식별자 (optional)

    private String unit; // ex: 1개, 1박스 등

    private String imageUrl; // 상품 이미지 URL

    public  void update(String name, String category, String unit, String imageUrl) {
        if (name != null) {
            this.name = name;
        }
        if (category != null) {
            this.category = category;
        }
        if (unit != null) {
            this.unit = unit;
        }
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }
}
