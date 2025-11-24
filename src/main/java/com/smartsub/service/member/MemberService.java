package com.smartsub.service.member;

import com.smartsub.domain.member.Member;
import com.smartsub.dto.member.MemberResponse;
import com.smartsub.dto.member.MemberUpdateRequest;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.slack.SlackUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SlackUserRepository slackUserRepository;


    public Long register(Member member) { // 회원가입 메서드
        validateDuplicateEmail(member.getEmail()); // 이메일 중복 체크

        String encodedPassword = passwordEncoder.encode(member.getPassword()); // 🔧 [수정] 비밀번호 암호화
        member.updatePassword(encodedPassword); // 🔧 [수정] 암호화된 비밀번호로 설정

        return memberRepository.save(member).getId(); // 회원 저장 후 ID 반환
    }

    private void validateDuplicateEmail(String email) { // 이메일 중복 체크 메서드
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다."); // 이메일 중복 시 예외 발생
        }
    }

    public MemberResponse findById(Long id) { // 회원 ID로 회원 조회
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")); // 회원 조회

        boolean slackConnected = slackUserRepository.existsByMember(member); // Slack 연동 여부 확인

        return MemberResponse.from(member, slackConnected); // MemberResponse 객체로 변환하여 반환
    }

    @Transactional
    public void updateMember(Long id, MemberUpdateRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")); // 회원 조회

        if (request.getName() != null) {
            member.updateName(request.getName()); // 이름 업데이트
        }
        if (request.getPassword() != null) {
            member.updatePassword(passwordEncoder.encode(request.getPassword())); // 🔧 [수정] 비밀번호 수정 시에도 암호화

        }
    }

    public void updateName(Long id, String newName) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        member.updateName(newName);
    }
}
