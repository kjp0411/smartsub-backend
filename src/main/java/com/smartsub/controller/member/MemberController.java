package com.smartsub.controller.member;

import com.smartsub.domain.member.Member;
import com.smartsub.dto.member.SignupRequest;
import com.smartsub.dto.member.SignupResponse;
import com.smartsub.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입: POST /api/members
    @PostMapping
    public ResponseEntity<SignupResponse> registerMember(@Valid @RequestBody SignupRequest request) {
        Member member = Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword()) // TODO: Service쪽에서 암호화
                .build();

        Long id = memberService.register(member);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new SignupResponse(id, "회원가입 성공"));
    }
}
