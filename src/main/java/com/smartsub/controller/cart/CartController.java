package com.smartsub.controller.cart;

import com.smartsub.dto.cart.CartItemRequest;
import com.smartsub.dto.cart.CartResponse;
import com.smartsub.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    private final CartService cartService;

    @GetMapping("/me")
    public ResponseEntity<CartResponse> myCart() {
        return ResponseEntity.ok(cartService.getMyCart());
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> add(@RequestBody CartItemRequest req) {
        return ResponseEntity.ok(cartService.add(req));
    }

    @PatchMapping("/items/{productId}")
    public ResponseEntity<CartResponse> qty(@PathVariable Long productId,
        @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateQty(productId, quantity));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> remove(@PathVariable Long productId) {
        return ResponseEntity.ok(cartService.remove(productId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clear() {
        cartService.clear();
        return ResponseEntity.noContent().build();
    }
}
