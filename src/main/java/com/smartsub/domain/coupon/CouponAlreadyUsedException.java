package com.smartsub.domain.coupon;

public class CouponAlreadyUsedException extends RuntimeException{
    public CouponAlreadyUsedException() {
        super("이미 사용된 쿠폰입니다.");
    }
}
