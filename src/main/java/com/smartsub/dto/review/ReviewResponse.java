package com.smartsub.dto.review;

import com.smartsub.domain.review.Review;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter // Getter 메서드를 자동으로 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 자동으로 생성
public class ReviewResponse {
    private Long id; // 리뷰 ID
    private Long memberId; // 리뷰 작성자 ID
    private String memberName; // 리뷰 작성자 이름
    private String content; // 리뷰 내용
    private int rating; // 평점
    private LocalDateTime createdAt; // 리뷰 작성 시간

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
            review.getId(),
            review.getMember().getId(),
            review.getMember().getName(),
            review.getContent(),
            review.getRating(),
            review.getCreatedAt()
        );
    }
}
