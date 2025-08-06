package com.smartsub.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsub.domain.slack.SlackUser;
import com.smartsub.dto.slack.SlackMessage;
import com.smartsub.repository.slack.SlackUserRepository;
import com.smartsub.service.slack.SlackDmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackKafkaConsumer {

    private final ObjectMapper objectMapper;
    private final SlackDmService slackDmService;
    private final SlackUserRepository slackUserRepository;  // ✅ 추가

    @KafkaListener(topics = "slack-message-topic", groupId = "slack-consumer")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            SlackMessage message = objectMapper.readValue(record.value(), SlackMessage.class);
            log.info("📥 Kafka 메시지 수신: {}", message);

            // ✅ accessToken은 DB에서 조회
            SlackUser slackUser = slackUserRepository.findBySlackUserId(message.getSlackUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 Slack 유저를 찾을 수 없습니다."));

            slackDmService.sendDirectMessage(
                message.getSlackUserId(),
                message.getMessage(),
                slackUser.getAccessToken()
            );

        } catch (Exception e) {
            log.error("Kafka 메시지 처리 실패", e);
        }
    }
}

