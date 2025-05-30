package com.smartsub.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // Getter 메서드를 자동으로 생성
@NoArgsConstructor // 기본 생성자를 자동으로 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 자동으로 생성
public class ReviewRequest {
    private Long productId; // 제품 ID
    private String content; // 리뷰 내용
    private int rating; // 평점
}
