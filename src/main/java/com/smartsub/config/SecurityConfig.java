package com.smartsub.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity // @PreAuthorize 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // ← @Component 빈

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsSource()))
            .headers(h -> h.frameOptions(frame -> frame.disable())) // H2 콘솔
            // JWT는 무상태 세션이어야 함
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 401/403 발생 시 JSON 응답으로 반환
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json;charset=UTF-8");
                    res.getWriter().write("{\"message\":\"Unauthorized\"}");
                })
                .accessDeniedHandler((req, res, e) -> {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.setContentType("application/json;charset=UTF-8");
                    res.getWriter().write("{\"message\":\"Forbidden\"}");
                })
            )
            .authorizeHttpRequests(auth -> auth
                // --- 공개 엔드포인트 ---
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/oauth/**").permitAll()
                .requestMatchers("/batch/**").permitAll() // 필요 시 API Key/IP 보호 권장
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/reviews/product/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                .requestMatchers(HttpMethod.POST,
                    "/api/auth/login",
                    "/api/auth/signup",
                    "/api/members" // 회원가입 경로가 이거면 허용
                ).permitAll()

                // --- 상품: 등록/수정/삭제는 ADMIN만 ---
                .requestMatchers(HttpMethod.POST,   "/api/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,  "/api/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                // --- 주문: 로그인 사용자면 가능 ---
                .requestMatchers(HttpMethod.POST, "/api/orders/**").authenticated()
                .requestMatchers("/api/cart/**").hasAnyRole("USER", "ADMIN")

                // --- 즐겨찾기: 모두 허용 ---
                .requestMatchers(HttpMethod.POST, "/api/favorites").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/favorites/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/favorites/**").permitAll()
                // --- 관리자 페이지 ---
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // --- 그 외 API는 인증 필요 ---
                .requestMatchers("/api/**").authenticated()

                // 정적/문서 등 기타는 허용
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // 운영에선 도메인 제한 권장
        configuration.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
