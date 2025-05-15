package com.smartsub.dto.anth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken; // 액세스 토큰
}
