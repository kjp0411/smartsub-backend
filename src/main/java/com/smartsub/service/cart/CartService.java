package com.smartsub.service.cart;

import com.smartsub.domain.cart.CartItem;
import com.smartsub.dto.cart.CartItemRequest;
import com.smartsub.dto.cart.CartLine;
import com.smartsub.dto.cart.CartResponse;
import com.smartsub.repository.cart.CartItemRepository;
import com.smartsub.repository.product.ProductRepository;
import com.smartsub.util.SecurityUtils; // 프로젝트 내 JWT에서 memberId 꺼내는 유틸 가정
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartRepo;
    private final ProductRepository productRepo;

    private Long currentMemberId() {
        return SecurityUtils.getCurrentMemberId();
    }

    @Transactional(readOnly = true)
    public CartResponse getMyCart() {
        Long memberId = currentMemberId();
        var items = cartRepo.findByMemberId(memberId);

        List<CartLine> lines = items.stream().map(ci -> {
            var p = productRepo.findById(ci.getProductId()).orElseThrow();
            int totalPrice = p.getPrice() * ci.getQuantity();
            return new CartLine(p.getId(), p.getName(), p.getPrice(), ci.getQuantity(), totalPrice);
        }).toList();

        int cartTotal = lines.stream().mapToInt(CartLine::getTotalPrice).sum();
        return new CartResponse(lines, cartTotal);
    }

    @Transactional
    public CartResponse add(CartItemRequest req) {
        Long memberId = currentMemberId();
        var p = productRepo.findById(req.getProductId()).orElseThrow();

        // 수량 정규화 (1~99)
        int raw = (req.getQuantity() == null) ? 1 : req.getQuantity();
        final int addQty = Math.max(1, Math.min(99, raw));  // effectively final

        // 이미 담긴 항목이 있는지 조회
        var existingOpt = cartRepo.findByMemberIdAndProductId(memberId, p.getId());
        if (existingOpt.isPresent()) {
            // 기존 수량에 addQty 더하기 (최대 99)
            var ci = existingOpt.get();
            ci.setQuantity(Math.min(99, ci.getQuantity() + addQty));
        } else {
            // 새로 담기
            var ci = new CartItem();
            ci.setMemberId(memberId);
            ci.setProductId(p.getId());
            ci.setQuantity(addQty);
            try {
                cartRepo.save(ci);
            } catch (DataIntegrityViolationException e) {
                // 드물게 동시성 경쟁으로 UNIQUE(member,product) 충돌 시 재시도
                var existed = cartRepo.findByMemberIdAndProductId(memberId, p.getId()).orElseThrow();
                existed.setQuantity(Math.min(99, existed.getQuantity() + addQty));
            }
        }

        return getMyCart();
    }


    @Transactional
    public CartResponse updateQty(Long productId, int quantity) {
        Long memberId = currentMemberId();
        int q = Math.max(1, Math.min(99, quantity));
        var ci = cartRepo.findByMemberIdAndProductId(memberId, productId).orElseThrow();
        ci.setQuantity(q);
        return getMyCart();
    }

    @Transactional
    public CartResponse remove(Long productId) {
        Long memberId = currentMemberId();
        cartRepo.deleteByMemberIdAndProductId(memberId, productId);
        return getMyCart();
    }

    @Transactional
    public void clear() {
        cartRepo.deleteByMemberId(currentMemberId());
    }
}
