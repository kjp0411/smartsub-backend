package com.smartsub.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // Getter 어노테이션을 사용하여 모든 필드에 대한 getter 메서드를 자동 생성
@Setter // Setter 어노테이션을 사용하여 모든 필드에 대한 setter 메서드를 자동 생성
@NoArgsConstructor // NoArgsConstructor 어노테이션을 사용하여 기본 생성자를 자동 생성
@AllArgsConstructor // AllArgsConstructor 어노테이션을 사용하여 모든 필드를 인자로 받는 생성자를 자동 생성
public class LoginRequest {

    private String email; // 이메일
    private String password; // 비밀번호
}
