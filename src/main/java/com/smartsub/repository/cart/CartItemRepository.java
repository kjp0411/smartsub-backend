package com.smartsub.repository.cart;

import com.smartsub.domain.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByMemberId(Long memberId);
    Optional<CartItem> findByMemberIdAndProductId(Long memberId, Long productId);
    void deleteByMemberIdAndProductId(Long memberId, Long productId);
    void deleteByMemberId(Long memberId);
}
