package com.smartsub.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequest {
    private String name;
    private String category;
    private String unit;
    private String imageUrl;

}
