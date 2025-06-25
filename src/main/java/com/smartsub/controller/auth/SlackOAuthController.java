package com.smartsub.controller.auth;

import com.smartsub.service.slack.SlackOAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class SlackOAuthController {

    @Value("${slack.client-id}")
    private String clientId;

    @Value("${slack.redirect-uri}")
    private String redirectUri;

    private final SlackOAuthService slackOAuthService;

    /**
     * Slack 인증 시작 요청
     * - 비회원일 경우 초대 링크로 이동
     * - 회원일 경우 smartsubhq 워크스페이스 인증 URL로 이동
     */
    @GetMapping("/oauth/slack/authorize")
    public void redirectToSlack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userEmail = extractEmailFromRequest(request); // JWT or Session 등에서 추출

        if (!isSlackMember(userEmail)) {
            // Slack 미가입자 → 초대 링크로 이동
            String inviteUrl = "https://join.slack.com/t/smartsubhq/shared_invite/zt-36nhfr2rm-6raCwfrIB4dkgb_rIWxSAQ";
            response.sendRedirect(inviteUrl);
            return;
        }

        // Slack 회원이면 OAuth 인증 URL로 리디렉션
        String workspaceDomain = "smartsubhq";
        String url = "https://" + workspaceDomain + ".slack.com/oauth/v2/authorize"
            + "?client_id=" + clientId
            + "&scope=chat:write,im:write"
            + "&user_scope=identity.basic"
            + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        response.sendRedirect(url);
    }

    /**
     * Slack OAuth 콜백 처리
     */
    @GetMapping("/oauth/slack/callback")
    public String handleSlackCallback(@RequestParam("code") String code) {
        slackOAuthService.processOAuthCallback(code);
        return "✅ Slack 인증이 완료되었습니다. 이제 개인 알림을 받을 수 있습니다.";
    }

    /**
     * (임시) 이메일 추출 로직 - 포트폴리오용이므로 하드코딩 가능
     */
    private String extractEmailFromRequest(HttpServletRequest request) {
        // 실제로는 JWT나 세션 등에서 꺼내야 함
        return "test@example.com"; // 포트폴리오용 하드코딩 예시
    }

    /**
     * Slack 워크스페이스 가입 여부 판단 (실제론 Slack API 필요)
     */
    private boolean isSlackMember(String email) {
        // 포트폴리오에서는 무조건 false로 해도 무방
        return false;
    }
}
