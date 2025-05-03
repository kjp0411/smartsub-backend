package com.smartsub.controller.member;

// 회원가입 메서드
// - 클라이언트로부터 회원가입 요청을 받고, 회원 정보를 저장하는 메서드
// - @PostMapping("/signup") 어노테이션을 사용하여 POST 요청을 처리
// - @RequestBody 어노테이션을 사용하여 요청 본문을 SignupRequest 객체로 변환
// - @Valid 어노테이션을 사용하여 요청 데이터 검증
// - @ResponseStatus(HttpStatus.CREATED) 어노테이션을 사용하여 응답 상태 코드를 201 Created로 설정
// - @ResponseBody 어노테이션을 사용하여 응답 본문을 SignupResponse 객체로 변환

import com.smartsub.domain.member.Member;
import com.smartsub.dto.member.SignupRequest;
import com.smartsub.dto.member.SignupResponse;
import com.smartsub.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // RESTful API 컨트롤러로 등록
@RequestMapping("/api/members") // API 경로 설정
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성 (Lombok)
public class MemberController {
    private final MemberService memberService; // MemberService 주입

    @PostMapping
    public ResponseEntity<SignupResponse> registerMember(@Valid @RequestBody SignupRequest request) {
        Member member = Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword()) // 비밀번호는 추후 암호화 처리 필요
                .build();

        Long id = memberService.register(member); // 회원 등록
        return ResponseEntity.status(201).body(new SignupResponse(id, "회원가입 성공")); // 응답 생성
    }
}
