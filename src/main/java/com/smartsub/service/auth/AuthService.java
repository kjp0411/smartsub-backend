package com.smartsub.service.auth;

import com.smartsub.domain.member.Member;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // 서비스 레이어를 나타내는 어노테이션
@RequiredArgsConstructor // 생성자 주입을 위한 어노테이션
public class AuthService {

    private final MemberRepository memberRepository; // 회원 정보를 저장하는 레포지토리
    private final PasswordEncoder passwordEncoder; // 비밀번호 인코더
    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성 및 검증을 위한 클래스

    public String login(String email, String rawPassword) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다.")); // 이메일로 회원 정보 조회

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다."); // 비밀번호 불일치 예외 처리
        }

        return jwtTokenProvider.generateToken(member); // JWT 토큰 생성

    }
}
