package com.smartsub.dto.purchase;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // Getter 어노테이션을 사용하여 모든 필드에 대한 getter 메서드를 자동 생성
@NoArgsConstructor // NoArgsConstructor 어노테이션을 사용하여 기본 생성자 자동 생성
@AllArgsConstructor // AllArgsConstructor 어노테이션을 사용하여 모든 필드를 매개변수로 받는 생성자 자동 생성
public class PurchaseRequest {

    private Long memberId; // 구매한 회원 ID
    private Long productId; // 구매한 상품 ID
    private LocalDateTime purchasedAt; // 구매 날짜
    private Integer quantity; // 구매 수량
    private Integer price; // 구매 가격
}
