package com.smartsub.controller.member;

import com.smartsub.dto.member.MemberResponse;
import com.smartsub.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
public class MemberAdminController {

    private final MemberService memberService;

    // 관리자: 회원 단건 조회 GET /api/admin/members/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // 관리자 권한 필요
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.findById(id));
    }
}
