package com.smartsub.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequest {
    @Email // 이메일 형식 검증
    @NotBlank // 비어있지 않아야 함
    private String email; // 회원의 이메일

    @NotBlank // 비어있지 않아야 함
    private String name; // 회원의 이름

    @NotBlank // 비어있지 않아야 함
    private String password; // 회원의 비밀번호
}
