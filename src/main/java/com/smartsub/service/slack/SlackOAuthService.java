package com.smartsub.service.slack;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsub.domain.member.Member;
import com.smartsub.domain.slack.SlackUser;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.slack.SlackUserRepository;
import jakarta.transaction.Transactional;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
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

    @Value("${slack.client-id}")
    private String clientId;

    @Value("${slack.client-secret}")
    private String clientSecret;

    @Value("${slack.redirect-uri}")
    private String redirectUri;   // 예: https://xxxx.ngrok-free.app/oauth/slack/callback

    /**
     * memberId 를 state 로 실어서 Slack OAuth URL 생성
     */
    public String buildAuthorizeUrl(Long memberId) {
        String encodedRedirect = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        String url =
            "https://slack.com/oauth/v2/authorize" +
                "?client_id=" + clientId +
                "&scope=chat:write,im:write,users:read" +
                "&user_scope=chat:write,im:write" +
                "&state=" + memberId +
                "&redirect_uri=" + encodedRedirect;
        return url;
    }

    /**
     * Slack 이 code + state 를 들고 callback 으로 들어왔을 때 처리
     */
    @Transactional
    public void processOAuthCallback(String code, Long memberId) {
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

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);

            String slackUserId = root.path("authed_user").path("id").asText();
            String slackAccessToken = root.path("authed_user").path("access_token").asText();

            Member member = memberRepository.findById(memberId).orElseThrow();

            // 같은 회원이 다시 연동하면 이전 레코드 삭제
            slackUserRepository.findByMemberId(memberId)
                .ifPresent(slackUserRepository::delete);

            SlackUser slackUser = SlackUser.builder()
                .member(member)
                .slackUserId(slackUserId)
                .accessToken(slackAccessToken)
                .build();

            slackUserRepository.save(slackUser);
        } catch (Exception e) {
            log.error("Slack OAuth 처리 실패", e);
            throw new IllegalStateException("Slack OAuth 처리 실패", e);
        }
    }
}
