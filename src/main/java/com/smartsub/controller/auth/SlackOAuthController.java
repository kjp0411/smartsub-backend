package com.smartsub.controller.auth;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequiredArgsConstructor
public class SlackOAuthController {

    @Value("${slack.client-id}")
    private String clientId;

    @Value("${slack.redirect-uri}")
    private String redirectUri;

    @Value("${slack.client-secret}")
    private String clientSecret;


    @GetMapping("/oauth/slack/authorize")
    public void redirectToSlack(HttpServletResponse response) throws IOException {
        String url = "https://slack.com/oauth/v2/authorize"
            + "?client_id=" + clientId
            + "&scope=chat:write,im:write"
            + "&user_scope=identity.basic"
            + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);

        response.sendRedirect(url);
    }
    @GetMapping("/oauth/slack/callback")
    public ResponseEntity<String> handleSlackCallback(@RequestParam("code") String code) throws IOException {
        String clientId = this.clientId;
        String clientSecret = this.clientSecret; // 아래 2번 참고
        String redirectUri = this.redirectUri;

        WebClient webClient = WebClient.create();

        String response = webClient.post()
            .uri("https://slack.com/api/oauth.v2.access")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("code", code)
                .with("client_id", clientId)
                .with("client_secret", clientSecret)
                .with("redirect_uri", redirectUri))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        return ResponseEntity.ok("슬랙 인증 완료!\n응답 내용: " + response);
    }

}
