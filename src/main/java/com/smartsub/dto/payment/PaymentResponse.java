package com.smartsub.dto.payment;

import com.smartsub.domain.payment.Payment;
import com.smartsub.domain.payment.PaymentStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter // Getter 어노테이션을 사용하여 모든 필드에 대한 getter 메서드를 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 자동 생성
public class PaymentResponse {

    private Long id; // 결제 ID
    private Long memberId; // 결제한 회원 ID
    private Integer amount; // 결제 금액
    private String paymentMethod; // 결제 방법 (예: 카드, 계좌이체 등)
    private PaymentStatus status; // 결제 상태 (예: 성공, 실패 등)
    private LocalDateTime paidAt; // 결제 날짜

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
            payment.getId(),
            payment.getMember().getId(),
            payment.getAmount(),
            payment.getPaymentMethod(),
            payment.getStatus(),
            payment.getPaidAt()
        );
    }
}
