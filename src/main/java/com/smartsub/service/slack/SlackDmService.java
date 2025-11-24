package com.smartsub.service.slack;

import com.smartsub.domain.slack.SlackUser;
import com.smartsub.repository.slack.SlackUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackDmService {

    private final SlackUserRepository slackUserRepository;
    private final WebClient webClient = WebClient.create("https://slack.com/api");

    // (1) memberId로 SlackUser를 찾아 DM
    public void sendByMemberId(Long memberId, String text) {
        SlackUser su = slackUserRepository.findByMemberId(memberId)
            .orElseThrow(() -> new IllegalStateException("슬랙 연동 정보 없음 (memberId=" + memberId + ")"));

        sendDmViaOpen(su.getSlackUserId(), su.getAccessToken(), text);
    }

    // (2) 사용자 토큰으로 개인 DM 보내기
    public void sendDmViaOpen(String slackUserId, String accessToken, String text) {
        try {
            String resp = webClient.post()
                .uri("/chat.postMessage")
                .headers(h -> h.setBearerAuth(accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("channel", slackUserId, "text", text))
                .retrieve()
                .bodyToMono(String.class)
                .block();

            log.info("Slack DM 응답: {}", resp);

        } catch (Exception e) {
            log.error("❌ Slack DM 전송 실패 (userId=" + slackUserId + ")", e);
        }
    }
}
