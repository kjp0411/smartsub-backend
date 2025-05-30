package com.smartsub.service.member;

import com.smartsub.domain.member.Member;
import com.smartsub.dto.member.MemberResponse;
import com.smartsub.dto.member.MemberUpdateRequest;
import com.smartsub.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 회원가입 메서드
// - 이메일, 이름, 비밀번호를 인자로 받아 회원을 생성하고 저장
// - 이메일 중복 체크 및 비밀번호 암호화 처리 포함
// - 성공 시 회원 ID와 메시지를 담은 SignupResponse 객체 반환
// - 실패 시 예외 처리 (예: 이메일 중복 시 예외 발생)
@Service // 서비스 레이어로 등록
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성 (Lombok)
@Transactional // 트랜잭션 관리
public class MemberService {

    private final MemberRepository memberRepository; // MemberRepository 주입
    private final PasswordEncoder passwordEncoder; // 🔧 [수정] 비밀번호 암호화를 위한 PasswordEncoder 추가


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
        return MemberResponse.from(member); // MemberResponse 객체로 변환하여 반환
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
