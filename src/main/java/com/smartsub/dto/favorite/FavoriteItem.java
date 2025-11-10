package com.smartsub.dto.favorite;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteItem {
    private Long productId;
    private String name;
    private Integer price;
    private LocalDateTime likedAt;
}
