package com.smartsub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SlackNotificationService {

    @Value("${slack.webhook.url}")
    private String webhookUrl;

    private final WebClient webClient = WebClient.create();

    public void sendNotification(String message) {
        webClient.post()
            .uri(webhookUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(Map.of("text", message))
            .retrieve()
            .bodyToMono(String.class)
            .block(); // block()으로 동기 전송 (테스트에 유용)
    }
}
