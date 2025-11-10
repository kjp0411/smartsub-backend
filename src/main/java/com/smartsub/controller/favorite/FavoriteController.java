package com.smartsub.controller.favorite;

import com.smartsub.dto.favorite.FavoriteListResponse;
import com.smartsub.dto.favorite.FavoriteRequest;
import com.smartsub.dto.favorite.FavoriteResponse;
import com.smartsub.service.favorite.FavoriteService;
import com.smartsub.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/me")
    public ResponseEntity<FavoriteListResponse> myFavorites() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        return ResponseEntity.ok(favoriteService.myFavorites(memberId));
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> addFavorite(@RequestBody FavoriteRequest request) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        FavoriteResponse response = favoriteService.addFavorite(memberId, request.getProductId());
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> remove(@PathVariable Long productId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        favoriteService.remove(memberId, productId);
        return ResponseEntity.noContent().build();
    }
}
