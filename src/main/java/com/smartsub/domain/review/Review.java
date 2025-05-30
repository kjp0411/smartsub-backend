package com.smartsub.domain.review;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id // 기본 키로 설정
    @GeneratedValue // 자동 생성되는 아이디
    private Long id; // 리뷰 ID

    @ManyToOne // 다대일 관계, 리뷰는 하나의 회원과 제품에 속함
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 리뷰 작성자

    @ManyToOne // 다대일 관계, 리뷰는 하나의 제품에 속함
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String content;
    private int rating;

    @CreatedDate
    private LocalDateTime createdAt;
}
