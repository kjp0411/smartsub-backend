package com.smartsub.service.slack;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsub.domain.member.Member;
import com.smartsub.domain.slack.SlackUser;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.slack.SlackUserRepository;

import com.smartsub.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackOAuthService {

    private final SlackUserRepository slackUserRepository;
    private final MemberRepository memberRepository;
    private final WebClient webClient = WebClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${slack.client-id}")
    private String clientId;

    @Value("${slack.client-secret}")
    private String clientSecret;

    @Value("${slack.redirect-uri}")
    private String redirectUri;

    public void processOAuthCallback(String code) {
        String jsonResponse = webClient.post()
            .uri("https://slack.com/api/oauth.v2.access")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("code", code)
                .with("client_id", clientId)
                .with("client_secret", clientSecret)
                .with("redirect_uri", redirectUri))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        log.info("Slack OAuth 응답: {}", jsonResponse);

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);

            String slackUserId = root.path("authed_user").path("id").asText();
            String slackAccessToken = root.path("authed_user").path("access_token").asText();

            // ✅ AFTER: JWT에서 memberId 추출
            String token = jwtTokenProvider.resolveToken(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()); // 예: Authorization 헤더에서 추출
            Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
            Member member = memberRepository.findById(memberId).orElseThrow();


            SlackUser slackUser = SlackUser.builder()
                .member(member)
                .slackUserId(slackUserId)
                .accessToken(slackAccessToken)
                .build();

            slackUserRepository.save(slackUser);
            member.setSlackUser(slackUser);
            memberRepository.save(member); // 이 줄이 실제로 DB 반영하는 핵심
            log.info("✅ Slack 사용자 정보 저장 완료: {}", slackUserId);

        } catch (Exception e) {
            log.error("Slack 사용자 정보 파싱 실패", e);
        }
    }
    public String resolveToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return null;

        HttpServletRequest request = attributes.getRequest();
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
