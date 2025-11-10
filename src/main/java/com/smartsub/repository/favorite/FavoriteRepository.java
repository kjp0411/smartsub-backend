package com.smartsub.repository.favorite;

import com.smartsub.domain.favorite.Favorite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
    Optional<Favorite> findByMemberIdAndProductId(Long memberId, Long productId);
    List<Favorite> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);
}
