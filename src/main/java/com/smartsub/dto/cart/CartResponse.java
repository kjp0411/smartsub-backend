package com.smartsub.dto.cart;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartResponse {
    private List<CartLine> items; // 장바구니 항목 리스트
    private int totalPrice; // 장바구니 총 가격
}
