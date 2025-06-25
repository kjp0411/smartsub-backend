package com.smartsub.util;

import com.smartsub.domain.member.Member;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.service.slack.SlackDmService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class SlackDmTestRunner implements CommandLineRunner {

    private final SlackDmService slackDmService;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void run(String... args) {
        // 현재 로그인된 사용자(테스트용) ID 추출 (실제 서비스에서는 컨트롤러/필터에서 처리됨)
        Long memberId = jwtTokenProvider.getMemberIdFromCurrentToken();

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        String message = """
            📬 *SmartSub 개인 알림 테스트*
            %s님, 정기 결제가 성공적으로 완료되었습니다. 🎉
            더 많은 혜택을 smartsub-alerts 채널에서도 확인해보세요!
            """.formatted(member.getName());

        slackDmService.sendPaymentDmToMember(member.getId(), message);
    }
}
