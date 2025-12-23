package com.smartsub.controller.slack;

import com.smartsub.service.slack.SlackOAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/oauth/slack")
@Slf4j
public class SlackOAuthController {

    private final SlackOAuthService slackOAuthService;

    @GetMapping("/authorize")
    public void authorize(@RequestParam("memberId") Long memberId,
        HttpServletResponse response) throws IOException {
        String url = slackOAuthService.buildAuthorizeUrl(memberId);
        response.sendRedirect(url);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(
        @RequestParam("code") String code,
        @RequestParam(value = "state", required = false) String state // state가 없을 수도 있게
    ) {
        if (state == null || state.isBlank()) {
            return ResponseEntity.badRequest().body("state(회원ID)가 비어 있습니다.");
        }

        Long memberId = Long.valueOf(state);
        slackOAuthService.processOAuthCallback(code, memberId);
        return ResponseEntity.ok("✅ Slack 인증이 완료되었습니다. 이제 개인 알림을 받을 수 있습니다.");
    }
}
