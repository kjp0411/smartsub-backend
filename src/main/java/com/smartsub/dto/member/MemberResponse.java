package com.smartsub.dto.member;

import com.smartsub.domain.member.Member;
import lombok.Getter;

@Getter
public class MemberResponse {
    private Long id;
    private String email;
    private String name;
    private boolean slackLinked;

    // 생성자 추가
    public MemberResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.slackLinked = member.getSlackUserId() != null; // Slack 연동 여부 확인
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member);
    }
}
