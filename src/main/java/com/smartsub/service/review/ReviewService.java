package com.smartsub.service.review;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.product.Product;
import com.smartsub.domain.review.Review;
import com.smartsub.dto.review.ReviewRequest;
import com.smartsub.dto.review.ReviewResponse;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.product.ProductRepository;
import com.smartsub.repository.review.ReviewRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service // 서비스 클래스로 지정
@RequiredArgsConstructor // 생성자 주입을 위한 어노테이션
public class ReviewService {

    private final MemberRepository memberRepository; // 회원 리포지토리 의존성 주입
    private final ProductRepository productRepository; // 제품 리포지토리 의존성 주입
    private final ReviewRepository reviewRepository; // 리뷰 리포지토리 의존성 주입

    public ReviewResponse createReview(ReviewRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원입니다.")); // 회원 조회
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제품입니다.")); // 제품 조회

        Review review = new Review();
        review.setMember(member); // 리뷰 작성자 설정
        review.setProduct(product); // 리뷰 제품 설정
        review.setContent(request.getContent()); // 리뷰 내용 설정
        review.setRating(request.getRating()); // 리뷰 평점 설정
        review.setCreatedAt(LocalDateTime.now()); // 리뷰 작성 시간 설정

        return ReviewResponse.from(reviewRepository.save(review)); // 리뷰 저장 후 응답 생성
    }

    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제품입니다."));

        List<Review> reviews = reviewRepository.findByProduct(product);

        return reviews.stream()
            .map(ReviewResponse::from)
            .toList();
    }

}
