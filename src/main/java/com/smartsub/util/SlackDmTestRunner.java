package com.smartsub.util;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.slack.SlackUser;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.slack.SlackUserRepository;
import com.smartsub.service.slack.SlackDmService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

//@Component
@RequiredArgsConstructor
public class SlackDmTestRunner implements CommandLineRunner {

    private final SlackDmService slackDmService;
    private final MemberRepository memberRepository;
    private final SlackUserRepository slackUserRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void run(String... args) {
        // 현재 로그인된 사용자 memberId
        Long memberId = jwtTokenProvider.getMemberIdFromCurrentToken();

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        SlackUser slackUser = slackUserRepository.findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("Slack OAuth 정보가 없습니다."));

        String message = """
            📬 *SmartSub 개인 알림 테스트*
            %s님, 정기 결제가 성공적으로 완료되었습니다. 🎉
            """.formatted(member.getName());

        // 최종 Slack DM 방식
        slackDmService.sendDmViaOpen(
            slackUser.getSlackUserId(),
            slackUser.getAccessToken(),
            message
        );
    }
}