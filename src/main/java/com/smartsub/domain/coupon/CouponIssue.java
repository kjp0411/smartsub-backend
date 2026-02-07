package com.smartsub.domain.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "coupon_issues",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"coupon_id", "member_id"})
    }
)
public class CouponIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    private LocalDateTime usedAt;

    protected CouponIssue() {
    }

    public CouponIssue(Long couponId, Long memberId) {
        this.couponId = couponId;
        this.memberId = memberId;
        this.issuedAt = LocalDateTime.now();
    }

    // 쿠폰 사용
    public void use() {
        if (this.usedAt != null) {
            throw new CouponAlreadyUsedException();
        }
        this.usedAt = LocalDateTime.now();
    }

    public boolean isUsed() {
        return this.usedAt != null;
    }

    public Long getId() {
        return id;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
