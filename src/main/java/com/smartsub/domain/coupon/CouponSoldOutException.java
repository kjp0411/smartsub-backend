package com.smartsub.domain.coupon;

public class CouponSoldOutException extends RuntimeException{
    public CouponSoldOutException() {
        super("쿠폰이 모두 소진되었습니다.");
    }
}
