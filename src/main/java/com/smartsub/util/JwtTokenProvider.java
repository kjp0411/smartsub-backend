package com.smartsub.util;

import com.smartsub.domain.member.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * JwtTokenProvider 종합본
 * - 신규 방식: roles 배열 + ver 포함 (권장)
 * - 기존 방식: 편의 메서드 유지(Deprecated)
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret; // 256bit 이상(32+ 바이트) 랜덤 문자열

    @Value("${jwt.expiration-seconds:3600}")
    private long expirationSeconds; // 만료(초) - 기본 1시간

    /** 공통 서명키 */
    private Key key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // =========================
    // ✅ 신규 방식 (권장)
    // =========================

    /** 로그인 성공 시 토큰 생성: sub=memberId, roles=["USER"/"ADMIN"], ver=tokenVersion */
    public String generateToken(Member m) {
        Instant now = Instant.now();
        return Jwts.builder()
            .setSubject(String.valueOf(m.getId()))
            .claim("roles", List.of(m.getRole().name()))
            .claim("ver", m.getTokenVersion())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
    }

    /** 서명/만료 검증 + Claims 파싱 */
    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token);
    }

    public Long getMemberId(Claims claims) {
        return Long.valueOf(claims.getSubject());
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }

    public Integer getTokenVersion(Claims claims) {
        return claims.get("ver", Integer.class);
    }

//    // =========================
//    // ⚠️ 구 방식(편의 메서드) - Deprecated
//    // =========================
//
//    /**
//     * @deprecated 신규 코드에서는 Member 기반 generateToken(Member)을 사용하세요.
//     * roles/ver 정보가 없는 단순 토큰을 생성합니다.
//     */
//    @Deprecated
//    public String generateToken(Long memberId) {
//        Instant now = Instant.now();
//        return Jwts.builder()
//            .setSubject(String.valueOf(memberId))
//            .setIssuedAt(Date.from(now))
//            .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
//            .signWith(key(), SignatureAlgorithm.HS256)
//            .compact();
//    }
//
//    /**
//     * @deprecated parse(token) → getMemberId(claims) 조합을 사용하세요.
//     */
    @Deprecated
    public Long getMemberIdFromToken(String token) {
        try {
            return getMemberId(parse(token).getBody());
        } catch (Exception e) {
            throw new RuntimeException("JWT 파싱 실패", e);
        }
    }
//
//    /**
//     * @deprecated 토큰 유효성은 parse(token) 호출로 대체됩니다(예외 처리).
//     */
//    @Deprecated
//    public boolean validateToken(String token) {
//        try {
//            parse(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    /**
//     * @deprecated 컨트롤러/서비스에선 SecurityContext(Authentication) 사용 권장.
//     * 요청 컨텍스트에서 Authorization 헤더를 직접 읽어 memberId 반환.
//     */
    @Deprecated
    public Long getMemberIdFromCurrentToken() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ra instanceof ServletRequestAttributes sra) {
            HttpServletRequest request = sra.getRequest();
            String token = resolveToken(request);
            return getMemberIdFromToken(token);
        }
        throw new RuntimeException("요청 컨텍스트가 없습니다.");
    }
//
//    /**
//     * @deprecated 필터 내부 또는 parse(token) 기반 흐름으로 대체 권장.
//     */
    @Deprecated
    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        throw new RuntimeException("JWT 토큰을 찾을 수 없습니다.");
    }
}
