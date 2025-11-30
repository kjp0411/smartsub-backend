package com.smartsub.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsub.dto.slack.SlackMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "slack-message-topic";

    public void send(SlackMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(TOPIC, json);
        } catch (JsonProcessingException e) {
            log.error("Kafka 메시지 직렬화 실패", e);
        }
    }
}
