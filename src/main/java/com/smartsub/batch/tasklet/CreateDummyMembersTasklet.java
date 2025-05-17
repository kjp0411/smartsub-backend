package com.smartsub.batch.tasklet;

import com.smartsub.domain.member.Member;
import com.smartsub.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateDummyMembersTasklet implements Tasklet {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        for (int i = 1; i <= 10; i++) {
            String email = "dummy" + i + "@test.com";
            String rawPassword = "password" + i;
            String name = "더미회원" + i;

            // 이미 존재하는 이메일은 스킵
            if (memberRepository.findByEmail(email).isPresent()) {
                continue;
            }

            Member member = Member.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(rawPassword))
                .build();

            memberRepository.save(member);
        }

        return RepeatStatus.FINISHED;
    }
}
