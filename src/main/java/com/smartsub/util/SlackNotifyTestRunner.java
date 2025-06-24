package com.smartsub.util;

import com.smartsub.service.slack.SlackWebhookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SlackNotifyTestRunner implements CommandLineRunner {

    private final SlackWebhookService slackService;

    public SlackNotifyTestRunner(SlackWebhookService slackService) {
        this.slackService = slackService;
    }

    @Override
    public void run(String... args) {
        String message = """
            📦 정기배송 알림 테스트
            SmartSub 정기구독 알림 시스템이 성공적으로 Slack과 연동되었습니다! 🎉
            👉 앞으로 여기에 정기 구매 추천 알림이 도착할 예정입니다.
            """;

        slackService.sendNotification(message);
    }
}
