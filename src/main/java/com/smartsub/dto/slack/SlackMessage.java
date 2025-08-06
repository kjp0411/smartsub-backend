package com.smartsub.dto.slack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessage {
    private String slackUserId;
    private String message;
    private String accessToken;

    public SlackMessage(String slackUserId, String message) {
        this.slackUserId = slackUserId;
        this.message = message;
    }
}
