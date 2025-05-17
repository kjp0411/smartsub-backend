package com.smartsub.controller.auth;

import com.smartsub.dto.auth.LoginRequest;
import com.smartsub.dto.auth.LoginResponse;
import com.smartsub.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService; // 인증 정보를 처리하는 서비스

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword()); // 로그인 서비스 호출
        return ResponseEntity.ok(new LoginResponse(token)); // 로그인 성공 시 200 응답 반환
    }
}
