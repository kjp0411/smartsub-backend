package com.smartsub.dto.slack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessage {
    private Long memberId;  // 15
    private String text;    // "xxx 님, 결제가 완료되었습니다."
}
