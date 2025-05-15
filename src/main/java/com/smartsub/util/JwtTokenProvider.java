package com.smartsub.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private Key secretKey; // 비밀 키
    private final long expirationMs = 1000 * 60 * 60; // 만료 시간 (1시간)

    @PostConstruct
    protected void init() {
        // 비밀 키 초기화
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // HS256 알고리즘에 맞는 비밀 키 생성
        // 실제 운영에서는 외부에서 비밀 키를 주입받는 것이 좋음
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs); // 만료 시간 설정

        return Jwts.builder()
            .setSubject(email) // JWT의 주체 (사용자 이메일)
            .setIssuedAt(now) // 발급 시간
            .setExpiration(expiry) // 만료 시간
            .signWith(secretKey) // 비밀 키로 서명
            .compact(); // JWT 생성
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey) // 비밀 키로 서명 검증
            .build()
            .parseClaimsJws(token) // JWT 파싱
            .getBody()
            .getSubject(); // 주체 (사용자 이메일) 반환
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token); // JWT 파싱 및 서명 검증
            return true; // 검증 성공
        } catch (Exception e) {
            return false; // 검증 실패
        }
    }
}
