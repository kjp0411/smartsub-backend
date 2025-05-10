package com.smartsub.domain.purchase;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class PurchaseHistory {

    @Id // JPA에서 기본 키로 사용할 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임
    private Long id; // 구매 이력 ID

    @ManyToOne(optional = false) // optional = false는 이 관계가 필수임을 나타냄
    @JoinColumn(name = "member_id") // member_id라는 외래 키로 매핑
    private Member member; // 구매한 회원

    @ManyToOne(optional = false) // optional = false는 이 관계가 필수임을 나타냄
    @JoinColumn(name = "product_id") // product_id라는 외래 키로 매핑
    private Product product; // 구매한 상품

    @Column(nullable = false) // null을 허용하지 않음
    private LocalDateTime purchasedAt; // 구매 날짜

    private Integer quantity; // 구매 수량

    private Integer price; // 구매 가격
}
