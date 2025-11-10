package com.smartsub.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter // Getter 어노테이션: 클래스의 모든 필드에 대해 getter 메서드를 자동 생성
@AllArgsConstructor // AllArgsConstructor 어노테이션: 모든 필드를 매개변수로 받는 생성자를 자동 생성
public class CartLine {
    private Long productId; // 상품 ID
    private String productName; // 상품 이름
    private int price; // 상품 가격
    private int quantity; // 수량
    private int totalPrice; // 총 가격 (price * quantity)
}
