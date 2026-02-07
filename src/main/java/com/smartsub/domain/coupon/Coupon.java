package com.smartsub.domain.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType type;

    @Column(nullable = false)
    private int totalCount;

    @Column(nullable = false)
    private int issuedCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    protected Coupon() {
    }

    public Coupon(String name, CouponType type, int totalCount) {
        this.name = name;
        this.type = type;
        this.totalCount = totalCount;
        this.issuedCount = 0;
        this.status = CouponStatus.ACTIVE;
    }

    public void issue() {
        if (this.type == CouponType.FIRST_COME) {
            issueFirstCome();
        }
        // Slack Auth 쿠폰은 발급 제한이 없으므로 별도 처리 없음
    }

    private void issueFirstCome() {
        if (this.status == CouponStatus.SOLD_OUT || this.issuedCount >= this.totalCount) {
            throw new CouponSoldOutException();
        }

        this.issuedCount++;

        if (this.issuedCount >= this.totalCount) {
            this.status = CouponStatus.SOLD_OUT;
        }
    }
}
