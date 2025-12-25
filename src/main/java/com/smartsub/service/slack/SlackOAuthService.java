package com.smartsub.service.slack;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsub.domain.member.Member;
import com.smartsub.domain.slack.SlackUser;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.slack.SlackUserRepository;
import jakarta.transaction.Transactional;
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

    // WebClient, ObjectMapper 그대로 사용
    private final WebClient webClient = WebClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${slack.client-id}")
    private String clientId;

    @Value("${slack.client-secret}")
    private String clientSecret;

    @Value("${slack.redirect-uri}")
    private String redirectUri;   // https://smartsub.dev/api/oauth/slack/callback

    public String buildAuthorizeUrl(Long memberId) {
        return "https://slack.com/oauth/v2/authorize"
            + "?client_id=" + clientId
            + "&scope=users:read"
            + "&user_scope=chat:write"
            + "&state=" + memberId
            + "&redirect_uri=" + redirectUri;
    }

    @Transactional
    public void processOAuthCallback(String code, Long memberId) {
        String jsonResponse = webClient.post()
            .uri("https://slack.com/api/oauth.v2.access")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("code", code)
                .with("client_id", clientId)
                .with("client_secret", clientSecret)
                .with("redirect_uri", redirectUri)) // ★ authorize와 동일한 redirect_uri 사용
            .retrieve()
            .bodyToMono(String.class)
            .block();

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            if (!root.path("ok").asBoolean()) {
                String error = root.path("error").asText();
                log.error("Slack OAuth 실패 응답: {}", error);
                throw new IllegalStateException("Slack OAuth 실패: " + error);
            }

            String slackUserId = root.path("authed_user").path("id").asText();
            String slackAccessToken = root.path("authed_user").path("access_token").asText();

            if (slackUserId.isBlank() || slackAccessToken.isBlank()) {
                throw new IllegalStateException("Slack OAuth 응답 값이 비어 있습니다.");
            }

            Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

            slackUserRepository.findByMemberId(memberId)
                .ifPresent(slackUserRepository::delete);

            SlackUser slackUser = SlackUser.builder()
                .member(member)
                .slackUserId(slackUserId)
                .accessToken(slackAccessToken)
                .build();

            slackUserRepository.save(slackUser);

            log.info("Slack OAuth 연동 완료 - memberId={}, slackUserId={}",
                memberId, slackUserId);

        } catch (Exception e) {
            log.error("Slack OAuth 처리 중 예외 발생", e);
            throw new IllegalStateException("Slack OAuth 처리 실패", e);
        }
    }
}
