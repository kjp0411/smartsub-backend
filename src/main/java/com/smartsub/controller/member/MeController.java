package com.smartsub.controller.member;

import com.smartsub.dto.member.MemberResponse;
import com.smartsub.dto.member.MemberUpdateRequest;
import com.smartsub.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController {

    private final MemberService memberService;

    // 내 정보 조회: GET /api/me
    @GetMapping
    public ResponseEntity<MemberResponse> getMyInfo(
        @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(memberService.findById(memberId));
    }

    // 내 정보 수정: PATCH /api/me
    @PatchMapping
    public ResponseEntity<String> updateMyInfo(
        @AuthenticationPrincipal Long memberId,
        @RequestBody MemberUpdateRequest request
    ) {
        memberService.updateMember(memberId, request);
        return ResponseEntity.ok("내 정보 수정 완료");
    }
}
