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

    private Key secretKey;
    private final long expirationMs = 1000 * 60 * 60; // 1시간

    @PostConstruct
    protected void init() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // ✅ memberId를 subject로 설정
    public String generateToken(Long memberId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
            .setSubject(String.valueOf(memberId)) // 🔄 memberId를 문자열로 저장
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey)
            .compact();
    }

    // ✅ JWT에서 memberId 꺼내기
    public Long getMemberIdFromToken(String token) {
        String subject = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();

        return Long.parseLong(subject); // 🔄 memberId로 변환
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
