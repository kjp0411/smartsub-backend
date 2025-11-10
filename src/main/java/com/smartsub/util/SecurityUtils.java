package com.smartsub.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /** 현재 SecurityContext에 저장된 Authentication */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 현재 로그인한 사용자의 memberId(Long) 반환
     * JwtAuthenticationFilter에서 Authentication의 principal을 memberId로 넣었다고 가정.
     */
    public static Long getCurrentMemberId() {
        Authentication auth = getAuthentication();
        if (auth == null || auth.getPrincipal() == null) return null;
        try {
            return Long.valueOf(auth.getPrincipal().toString());
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                "JWT principal은 memberId(Long) 이어야 합니다. 현재 값: " + auth.getPrincipal()
            );
        }
    }

    /** 현재 사용자가 특정 ROLE을 가지고 있는지 (예: hasRole(\"MEMBER\")) */
    public static boolean hasRole(String role) {
        Authentication auth = getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(r -> r.equals("ROLE_" + role));
    }
}
