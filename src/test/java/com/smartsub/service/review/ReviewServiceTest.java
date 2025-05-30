package com.smartsub.service.review;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.product.Product;
import com.smartsub.domain.review.Review;
import com.smartsub.dto.review.ReviewRequest;
import com.smartsub.dto.review.ReviewResponse;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.product.ProductRepository;
import com.smartsub.repository.review.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private ReviewService reviewService;
    private MemberRepository memberRepository;
    private ProductRepository productRepository;
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        productRepository = mock(ProductRepository.class);
        reviewRepository = mock(ReviewRepository.class);
        reviewService = new ReviewService(memberRepository, productRepository, reviewRepository);
    }

    @Test
    void 리뷰_생성_성공() {
        Member member = Member.builder().id(1L).name("테스터").build();
        Product product = Product.builder().id(100L).name("테스트 상품").build();
        ReviewRequest request = new ReviewRequest(100L, "좋아요!", 5);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReviewResponse response = reviewService.createReview(request, 1L);

        assertThat(response.getContent()).isEqualTo("좋아요!");
        assertThat(response.getRating()).isEqualTo(5);
        assertThat(response.getMemberName()).isEqualTo("테스터");
    }

    @Test
    void 상품별_리뷰_조회_성공() {
        Product product = Product.builder().id(100L).name("테스트 상품").build();
        Member member = Member.builder().id(1L).name("리뷰어").build();

        Review review = Review.builder()
            .id(1L)
            .product(product)
            .member(member)
            .content("만족합니다.")
            .rating(4)
            .build();

        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(reviewRepository.findByProduct(product)).thenReturn(List.of(review));

        List<ReviewResponse> result = reviewService.getReviewsByProductId(100L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).isEqualTo("만족합니다.");
        assertThat(result.get(0).getRating()).isEqualTo(4);
    }
}
