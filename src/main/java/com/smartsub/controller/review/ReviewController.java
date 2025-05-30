package com.smartsub.controller.review;

import com.smartsub.dto.review.ReviewRequest;
import com.smartsub.dto.review.ReviewResponse;
import com.smartsub.service.review.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
    origins = "http://localhost:3000",
    allowedHeaders = "*",
    exposedHeaders = "*",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 🔍 예외 상황 체크
        if (authentication == null || !authentication.isAuthenticated() ||
            authentication.getPrincipal() == null ||
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("인증된 사용자만 리뷰를 작성할 수 있습니다.");
        }

        // ✅ Principal에서 memberId 추출
        Long memberId = Long.parseLong((String) authentication.getPrincipal());

        return ResponseEntity.ok(reviewService.createReview(request, memberId));
    }

    // ✅ 특정 상품의 리뷰 목록 조회
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }
}
