package com.smartsub.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.smartsub.domain.member.Member;
import com.smartsub.dto.member.MemberResponse;
import com.smartsub.repository.member.MemberRepository;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository, passwordEncoder);
    }

    @Test
    void 회원가입_성공() {
        // given
        Member member = Member.builder()
            .email("test@example.com")
            .name("홍길동")
            .password("1234")
            .build();

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.empty());
        when(memberRepository.save(any())).thenReturn(Member.builder().id(1L).build());

        // when
        Long savedId = memberService.register(member);

        // then
        assertThat(savedId).isEqualTo(1L);
    }

    @Test
    void 이메일중복_예외() {
        // given
        Member member = Member.builder()
            .email("test@example.com")
            .name("홍길동")
            .password("1234")
            .build();

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        // when, then
        assertThatThrownBy(() -> memberService.register(member))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("이미 존재하는 이메일입니다.");
    }

    @Test
    void 회원_조회_성공() {
        // given
        Member member = Member.builder()
            .id(1L)
            .email("test@example.com")
            .name("홍길동")
            .password("1234")
            .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // when
        MemberResponse result = memberService.findById(1L);

        // then
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void 회원_조회_실패_예외() {
        // given
        when(memberRepository.findById(999L)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.findById(999L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 회원이 존재하지 않습니다.");
    }

    @Test
    void 회원_정보_수정_성공() {
        // given
        Long id = 1L;
        Member member = Member.builder()
            .id(id)
            .email("test@example.com")
            .name("기존이름")
            .password("1234")
            .build();

        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        // when
        memberService.updateName(id, "새이름");

        // then
        assertThat(member.getName()).isEqualTo("새이름");
    }
}
