package com.smartsub.repository.review;

import com.smartsub.domain.payment.Payment;
import com.smartsub.domain.product.Product;
import com.smartsub.domain.review.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByPayment(Payment payment);
    List<Review> findByProductId(Long productId); // 제품 ID로 리뷰 조회
    List<Review> findByMemberId(Long memberId); // 회원 ID로 리뷰 조회
    List<Review> findByProduct(Product product);

}
