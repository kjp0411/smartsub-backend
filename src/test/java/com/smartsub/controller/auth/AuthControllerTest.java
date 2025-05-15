package com.smartsub.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsub.config.SecurityConfig;
import com.smartsub.dto.anth.LoginRequest;
import com.smartsub.dto.anth.LoginResponse;
import com.smartsub.service.auth.AuthService;
import com.smartsub.util.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class) // 시큐리티 설정 적용
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider; // Jwt 필터에서 사용되므로 추가

    @Test
    void 로그인_성공_테스트() throws Exception {
        // given
        LoginRequest request = new LoginRequest();
        request.setEmail("testuser@test.com");
        request.setPassword("1234");

        LoginResponse response = new LoginResponse("mocked-token");

        given(authService.login(anyString(), anyString())).willReturn(response.getAccessToken());

        // when & then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("mocked-token"));
    }
}
