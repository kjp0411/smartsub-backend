package com.smartsub.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsub.dto.slack.SlackMessage;
import com.smartsub.service.slack.SlackDmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackKafkaConsumer {

    private final SlackDmService slackDmService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // groupId는 애노테이션에서 지우고, application.properties의 spring.kafka.consumer.group-id를 사용 권장
    @KafkaListener(topics = "slack-message-topic")
    public void consume(String payload) {
        try {
            SlackMessage msg = objectMapper.readValue(payload, SlackMessage.class);

            // SlackMessage가 slackUserId가 아니라 memberId를 들고 오는 형태라면,
            // DM 보낼 때 memberId로 SlackUser를 조회해서 전송하도록 SlackDmService를 사용
            slackDmService.sendByMemberId(
                msg.getMemberId(),    // Long memberId
                msg.getText()         // String text
            );
        } catch (Exception e) {
            log.error("Kafka 수신 처리 실패: {}", payload, e);
        }
    }
}
