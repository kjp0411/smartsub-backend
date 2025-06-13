package com.smartsub.domain.subscription;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity // JPA 엔티티로 사용하기 위해 @Entity 어노테이션 추가
@Getter // 엔티티 클래스이므로 Getter를 사용하여 필드 접근
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자 접근 제한
@AllArgsConstructor // 모든 필드를 초기화하는 생성자 추가
@Builder // 빌더 패턴을 사용하여 객체 생성
public class Subscription {

    @Id // 엔티티의 기본 키를 나타내기 위해 @Id 어노테이션 추가
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성 전략 설정
    private Long id; // 구독 ID

    @ManyToOne(fetch = FetchType.LAZY) // 회원과 다대일 관계 설정
    private Member member; // 구독한 회원 정보

    @ManyToOne(fetch = FetchType.LAZY) // 상품과 다대일 관계 설정
    private Product product; // 구독한 상품 정보

    private int cycleDays; // 정기 결제 주기 (ex: 7일, 30일)

    private LocalDate lastPaidDate; // 마지막 결제 날짜

    private boolean active; // 구독 활성화 여부 (true: 활성화, false: 비활성화)

    private LocalTime preferredTime; // 선호하는 결제 시간 (ex: 매일 오전 9시)

    public boolean isPaymentDueToday(LocalDate today) { // 오늘 결제 예정 여부 확인 메서드
        return active && lastPaidDate != null
            && lastPaidDate.plusDays(cycleDays).isEqual(today);
    }

    public void markPaid(LocalDate paidDate) {
        this.lastPaidDate = paidDate;
    }

    public void cancel() {
        this.active = false; // 구독 비활성화
    }
}
