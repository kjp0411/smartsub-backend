package com.smartsub.dto.favorite;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteResponse {
    private Long favoriteId;
    private Long productId;
    private LocalDateTime createdAt;
}
