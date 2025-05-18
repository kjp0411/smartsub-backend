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

@Entity // JPA 엔티티로 지정
@Getter // Getter 어노테이션을 사용하여 모든 필드에 대한 getter 메서드를 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 (외부 생성을 제한)
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 자동 생성
@Builder // Builder 패턴을 사용하여 객체 생성 가능
public class Payment {

    @Id // JPA에서 기본 키로 사용할 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임
    private Long id; // 결제 ID

    @ManyToOne(optional = false) // optional = false는 이 관계가 필수임을 나타냄
    @JoinColumn(name = "member_id") // member_id라는 외래 키로 매핑
    private Member member; // 결제한 회원

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id") // 외래 키 설정
    private Product product;

    @Column(nullable = false) // null을 허용하지 않음
    private Integer amount; // 결제 금액

    @Column(nullable = false, length = 30) // 결제 수단은 null을 허용하지 않으며, 최대 길이는 30
    private String paymentMethod; // 결제 수단 (예: 카드, 계좌이체 등)

    @Enumerated(EnumType.STRING) // Enum 타입을 문자열로 저장
    @Column(nullable = false, length = 20) // 결제 상태는 null을 허용하지 않으며, 최대 길이는 20
    private PaymentStatus status; // 결제 상태 (예: 성공, 실패 등)

    private LocalDateTime paidAt; // 결제 날짜

    public void markSuccess() {
        this.status = PaymentStatus.SUCCESS; // 결제 성공으로 상태 변경
        this.paidAt = LocalDateTime.now(); // 결제 날짜를 현재 시간으로 설정
    }

    public void markFailed() {
        this.status = PaymentStatus.FAILED; // 결제 실패로 상태 변경
    }
}
