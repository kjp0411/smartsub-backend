package com.smartsub.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter // 모든 필드에 대한 getter 자동 생성 (Lombok)
/**
 * 회원가입 응답 DTO 클래스
 * - 회원가입 성공 시 클라이언트에게 전달되는 응답 데이터 구조
 * - 회원의 ID와 응답 메시지를 포함
 */
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 자동 생성
public class SignupResponse {
    private Long id; // 회원의 ID
    private String message; // 응답 메시지
}
