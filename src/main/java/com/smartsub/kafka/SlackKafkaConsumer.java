package com.smartsub.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsub.dto.slack.SlackMessage;
import com.smartsub.service.slack.SlackDmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Profile("kafka")   // Kafka 사용 시에만 활성화
@Component
@RequiredArgsConstructor
@Slf4j
public class SlackKafkaConsumer {

    private final SlackDmService slackDmService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "slack-message-topic")
    public void consume(String payload) {
        try {
            SlackMessage msg = objectMapper.readValue(payload, SlackMessage.class);

            slackDmService.sendByMemberId(
                msg.getMemberId(),
                msg.getText()
            );
        } catch (Exception e) {
            log.error("Kafka 수신 처리 실패: {}", payload, e);
        }
    }
}
