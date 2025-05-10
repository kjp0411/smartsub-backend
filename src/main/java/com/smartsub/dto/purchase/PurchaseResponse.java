package com.smartsub.dto.purchase;

import com.smartsub.domain.purchase.PurchaseHistory;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter // Getter 어노테이션을 사용하여 모든 필드에 대한 getter 메서드를 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 자동 생성
public class PurchaseResponse {

    private Long id; // 구매 ID
    private Long memberId; // 구매한 회원 ID
    private Long productId; // 구매한 상품 ID
    private LocalDateTime purchasedAt; // 구매 날짜
    private Integer quantity; // 구매 수량
    private Integer price; // 구매 가격

    public static PurchaseResponse from(PurchaseHistory history) {
        return new PurchaseResponse(
            history.getId(),
            history.getMember().getId(),
            history.getProduct().getId(),
            history.getPurchasedAt(),
            history.getQuantity(),
            history.getPrice()
        );
    }
}
