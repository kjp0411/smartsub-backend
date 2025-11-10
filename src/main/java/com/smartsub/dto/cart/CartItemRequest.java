package com.smartsub.dto.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // Getter 어노테이션: 클래스의 모든 필드에 대해 getter 메서드를 자동 생성
@Setter // Setter 어노테이션: 클래스의 모든 필드에 대해 setter 메서드를 자동 생성
@NoArgsConstructor // NoArgsConstructor 어노테이션: 기본 생성자를 자동 생성
public class CartItemRequest {
    private Long productId; // 상품 ID
    private Integer quantity; // 수량의 기본값 1, null이면 서비스단에서 처리
}
