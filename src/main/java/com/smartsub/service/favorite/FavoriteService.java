package com.smartsub.service.favorite;

import com.smartsub.domain.favorite.Favorite;
import com.smartsub.domain.product.Product;
import com.smartsub.dto.favorite.FavoriteItem;
import com.smartsub.dto.favorite.FavoriteListResponse;
import com.smartsub.dto.favorite.FavoriteResponse;
import com.smartsub.repository.favorite.FavoriteRepository;
import com.smartsub.repository.product.ProductRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public FavoriteListResponse myFavorites(Long memberId) {
        // 1) 회원의 즐겨찾기 최신순
        List<Favorite> favorites = favoriteRepository
            .findAllByMemberIdOrderByCreatedAtDesc(memberId);

        if (favorites.isEmpty()) {
            return new FavoriteListResponse(List.of(), 0);
        }

        // 2) N+1 방지: productId 한 번에 조회
        List<Long> productIds = favorites.stream()
            .map(Favorite::getProductId)
            .distinct()
            .toList();

        Map<Long, Product> productMap = productRepository.findAllById(productIds)
            .stream()
            .collect(Collectors.toMap(Product::getId, Function.identity()));

        // 3) DTO 매핑
        List<FavoriteItem> items = favorites.stream()
            .map(f -> {
                Product p = productMap.get(f.getProductId());
                // 제품이 삭제된 경우는 스킵(원하면 예외로 바꿔도 됨)
                if (p == null) return null;
                return new FavoriteItem(p.getId(), p.getName(), p.getPrice(), f.getCreatedAt());
            })
            .filter(Objects::nonNull)
            .toList();

        return new FavoriteListResponse(items, items.size());
    }

    @Transactional
    public FavoriteResponse addFavorite(Long memberId, Long productId) {
        // 이미 즐겨찾기에 있으면 예외 발생
        if (favoriteRepository.existsByMemberIdAndProductId(memberId, productId)) {
            throw new IllegalStateException("이미 즐겨찾기에 등록된 상품입니다.");
        }
        // 새 엔티티 생성
        Favorite favorite = new Favorite();
        favorite.setMemberId(memberId);
        favorite.setProductId(productId);
        // 저장
        Favorite saved = favoriteRepository.save(favorite);
        // DTO로 변환해서 반환
        return new FavoriteResponse(saved.getId(), saved.getProductId(), saved.getCreatedAt());
    }

    @Transactional
    public void remove(Long memberId, Long productId) {
        favoriteRepository.findByMemberIdAndProductId(memberId, productId)
            .ifPresentOrElse(
                favoriteRepository::delete,
                () -> {
                    throw new IllegalStateException("즐겨찾기에 없는 상품입니다.");
                }
            );
    }
}
