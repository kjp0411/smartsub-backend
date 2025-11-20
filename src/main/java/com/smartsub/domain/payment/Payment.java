package com.smartsub.domain.payment;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
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
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // 결제 ID

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;          // 결제한 회원 ID

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;        // 결제한 상품 ID

    @Column(nullable = false)
    private Integer quantity;       // 결제한 상품 수량

    @Column(nullable = false)
    private Integer amount;         // 총 결제 금액

    @Column(nullable = false, length = 30)
    private String paymentMethod;   // 결제 방법 (예: CARD, KAKAO_PAY, 계좌 이체 등)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;   // 결제 상태 (예: PENDING, SUCCESS, FAILED)

    private LocalDateTime paidAt;   // 결제 완료 날짜 및 시간

    public void markSuccess() {
        this.status = PaymentStatus.SUCCESS;    // 결제 성공으로 상태 변경
        this.paidAt = LocalDateTime.now();      // 결제 날짜를 현재 시간으로 설정
    }

    public void markFailed() {
        this.status = PaymentStatus.FAILED;     // 결제 실패로 상태 변경
    }
}
