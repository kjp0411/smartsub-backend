package com.smartsub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifyTarget {
    private Long member_id;
    private String product_name;
    private String message;
}
