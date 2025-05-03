package com.smartsub.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.smartsub.domain.member.Member;
import com.smartsub.repository.member.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// MemberServiceTest 클래스는 회원 서비스에 대한 테스트를 수행하는 클래스입니다.
// 이 클래스는 JUnit 5를 사용하여 테스트를 작성합니다.
// @SpringBootTest 어노테이션을 사용하여 Spring Boot 애플리케이션 컨텍스트를 로드하고, @Autowired 어노테이션을 사용하여 MemberService를 주입받습니다.
// @Test 어노테이션을 사용하여 각 테스트 메서드를 정의합니다.
// 각 테스트 메서드는 회원 가입, 로그인, 이메일 중복 체크 등의 기능을 테스트합니다.
// 테스트 메서드에서는 assertEquals, assertThrows 등의 메서드를 사용하여 예상 결과와 실제 결과를 비교합니다.

class MemberServiceTest {
    // 회원가입 테스트
    private MemberRepository memberRepository;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class); // 가짜 객체 생성
        memberService = new MemberService(memberRepository); // MemberService 객체 생성
    }

    @Test
    void 회원가입_성공() {
        // given
        Member member = Member.builder()
            .email("test@example.com")
            .name("홍길동")
            .password("1234")
            .build();

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.empty()); // 이메일 중복 없음
        when(memberRepository.save(any())).thenReturn(Member.builder().id(1L).build()); // 회원 저장

        // when
        Long savedId = memberService.register(member);

        // then
        assertThat(savedId).isEqualTo(1L); // 저장된 ID가 1L인지 확인
    }

    @Test
    void 이메일중복_예외() {
        // given
        Member member = Member.builder()
            .email("test@example.com")
            .name("홍길동")
            .password("1234")
            .build();

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member)); // 이메일 중복 있음

        // when, then
        assertThatThrownBy(() -> memberService.register(member))
            .isInstanceOf(IllegalStateException.class) // 예외 발생 확인
            .hasMessage("이미 존재하는 이메일입니다."); // 예외 메시지 확인
    }
}