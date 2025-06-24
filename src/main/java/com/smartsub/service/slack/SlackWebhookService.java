package com.smartsub.service.slack;

import com.smartsub.domain.subscription.Subscription;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SlackWebhookService {

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

    public void sendSubscriptionPaymentSummary(List<Subscription> processedSubscriptions) {
        StringBuilder sb = new StringBuilder("✅ *[정기 결제 결과 요약]*\n");
        sb.append("총 ").append(processedSubscriptions.size()).append("건 결제 완료\n");

        for (Subscription s : processedSubscriptions) {
            sb.append("- ")
                .append(s.getMember().getId()).append("번 회원: ")
                .append("'").append(s.getProduct().getName()).append("' (")
                .append(s.getProduct().getPrice()).append("원)\n");
        }

        sendNotification(sb.toString());
    }

}
