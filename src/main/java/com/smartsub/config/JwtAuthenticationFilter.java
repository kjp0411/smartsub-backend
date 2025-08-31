package com.smartsub.config;

import com.smartsub.util.JwtTokenProvider;
import com.smartsub.repository.member.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    private static final AntPathMatcher matcher = new AntPathMatcher();
    private static final String[] SKIP_PATHS = {
        "/batch/**", "/h2-console/**", "/actuator/**", "/api/auth/**"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        for (String p : SKIP_PATHS) {
            if (matcher.match(p, path)) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws ServletException, IOException {

        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        String token = header.substring(7);
        try {
            var jws = jwtTokenProvider.parse(token);
            var claims = jws.getBody();

            Long memberId = jwtTokenProvider.getMemberId(claims);

            Integer ver = jwtTokenProvider.getTokenVersion(claims);
            if (ver != null) {
                int current = memberRepository.findTokenVersionById(memberId);
                if (ver != current) throw new RuntimeException("stale token");
            }

            var authorities = jwtTokenProvider.getRoles(claims).stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r)) // ROLE_USER / ROLE_ADMIN
                .toList();

            var auth = new UsernamePasswordAuthenticationToken(memberId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            SecurityContextHolder.clearContext(); // 서명/만료/버전 불일치 등
        }

        chain.doFilter(req, res);
    }
}
