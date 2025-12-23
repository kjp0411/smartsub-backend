package com.smartsub.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequest {
    private String name;
    private String category1;
    private String category2;
    private String category3;
    private String imageUrl;

}
