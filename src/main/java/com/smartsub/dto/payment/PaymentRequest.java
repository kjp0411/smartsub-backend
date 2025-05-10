package com.smartsub.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // Getter 어노테이션을 사용하여 모든 필드에 대한 getter 메서드를 자동 생성
@NoArgsConstructor // NoArgsConstructor 어노테이션을 사용하여 기본 생성자 자동 생성
@AllArgsConstructor // AllArgsConstructor 어노테이션을 사용하여 모든 필드를 매개변수로 받는 생성자 자동 생성
public class PaymentRequest {

    private Long memberId; // 결제한 회원 ID
    private Integer amount; // 결제 금액
    private String paymentMethod; // 결제 방법 (예: 카드, 계좌이체 등)
}
