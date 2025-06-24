package com.smartsub.util;

import com.smartsub.service.slack.SlackDmService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SlackDmTestRunner implements CommandLineRunner {

    private final SlackDmService slackDmService;

    @Override
    public void run(String... args) {
        // 실제 Slack 사용자 ID (예: 김종표 계정)
        String slackUserId = "U0922HQPN9M"; // 여기에 실제 유저 ID 입력

        String message = """
            📬 *SmartSub 개인 알림 테스트*
            김종표님, 정기 결제가 성공적으로 완료되었습니다. 🎉
            더 많은 혜택을 smartsub-alerts 채널에서도 확인해보세요!
            """;

        slackDmService.sendMessageToUser(slackUserId, message);
    }
}
