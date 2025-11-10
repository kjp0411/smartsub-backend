package com.smartsub.dto.favorite;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteListResponse {
    private List<FavoriteItem> items;
    private int count;
}
