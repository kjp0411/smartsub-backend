package com.smartsub.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private Long productId;         // 구매한 상품 ID
    private Integer quantity;       // 구매한 상품 수량
    private Integer amount;         // 총 결제 금액
    private String paymentMethod;   // 결제 수단 (예: 카드, 계좌이체 등)
}
