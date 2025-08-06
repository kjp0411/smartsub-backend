package com.smartsub.service.slack;

import com.smartsub.domain.slack.SlackUser;
import com.smartsub.repository.slack.SlackUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SlackDmService {

    @Value("${slack.bot-token}")
    private String botToken;

    private final WebClient webClient = WebClient.builder()
        .baseUrl("https://slack.com/api")
        .defaultHeader("Authorization", "Bearer " + "DYNAMICALLY_SET") // 실제 전송 시 헤더 동적으로 설정
        .build();

    /**
     * DM 채널을 열고 사용자에게 메시지 전송
     */
    public void sendMessageToUser(String userId, String message) {
        String url = "https://slack.com/api/chat.postMessage";

        Map<String, Object> payload = Map.of(
            "channel", userId,
            "text", message
        );

        webClient.post()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + botToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(payload)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }


    /**
     * 사용자와의 DM 채널을 오픈하는 Slack API 호출
     */
    private String openImChannel(String userId) {
        Map<String, String> body = Map.of("user", userId);

        return WebClient.create("https://slack.com/api")
            .post()
            .uri("/conversations.open")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + botToken)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> {
                Boolean ok = (Boolean) response.get("ok");
                if (ok != null && ok) {
                    Map<String, Object> channel = (Map<String, Object>) response.get("channel");
                    return (String) channel.get("id");
                }
                return null;
            })
            .block();
    }

    /**
     * DM 채널에 메시지 전송
     */
    private void sendMessage(String channelId, String message) {
        Map<String, String> body = Map.of(
            "channel", channelId,
            "text", message
        );

        WebClient.create("https://slack.com/api")
            .post()
            .uri("/chat.postMessage")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + botToken)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(String.class)
            .doOnNext(System.out::println)
            .block();
    }

    // SlackDmService 내부에 SlackUserRepository 주입
    private final SlackUserRepository slackUserRepository;

    public void sendPaymentDmToMember(Long memberId, String message) {
        SlackUser slackUser = slackUserRepository.findByMemberId(memberId)
            .orElseThrow(() -> new IllegalStateException("슬랙 사용자 정보 없음"));

        String accessToken = slackUser.getAccessToken();
        String slackUserId = slackUser.getSlackUserId();

        webClient.post()
            .uri("https://slack.com/api/chat.postMessage")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(Map.of("channel", slackUserId, "text", message))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public void sendDirectMessage(String slackUserId, String message, String accessToken) {
        Map<String, String> body = Map.of(
            "channel", slackUserId,
            "text", message
        );

        WebClient.create("https://slack.com/api")
            .post()
            .uri("/chat.postMessage")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(String.class)
            .doOnNext(res -> System.out.println("Slack 전송 응답: " + res))
            .block();
    }


}
