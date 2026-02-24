package org.songeun.petdongne_server.security;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.testSupport.AuthTestFixture;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.songeun.petdongne_server.security.session.SessionConfig.SESSION_COOKIE_NAME;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SecurityConfigIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FrontendUrlProperties frontendUrlProperties;

    @Autowired
    private AuthTestFixture authTestFixture;

    @DisplayName("유효한 토큰으로 보호된 엔드포인트에 접근하면 200 OK를 반환한다.")
    @Test
    void shouldAllowAccessToProtectedEndpointWithValidToken() throws Exception {
        // given
        String sessionId = authTestFixture.getSessionId();
        authTestFixture.resetSession(sessionId);
        Cookie sessionCookie = authTestFixture.getSessionCookie(sessionId);

        // when & then
        mockMvc.perform(get("/api/v1/buildings/1/reviews/1")
                        .cookie(sessionCookie))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("유효하지 않은 토큰으로 보호된 엔드포인트에 접근하면 401 Unauthorized를 반환한다.")
    @Test
    void shouldRejectAccessToProtectedEndpointWithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/v1/buildings/1/reviews/1")
                        .cookie(new Cookie(SESSION_COOKIE_NAME, "invalid_value")))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"));
    }

    @DisplayName("토큰 없이 보호된 엔드포인트에 접근하면 401 Unauthorized를 반환한다.")
    @Test
    void shouldRejectAccessToProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/buildings/1/reviews"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"));
    }

    @DisplayName("토큰 없이 공개된 엔드포인트에 접근하면 200 OK를 반환한다.")
    @Test
    void shouldAllowAccessToPublicEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/map/clusters?geoHashes=abc&level=11"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("허용된 Origin의 CORS Preflight 요청에 대해 200 OK를 반환한다.")
    @Test
    void shouldAllowCorsPreflightFromAllowedOrigin() throws Exception {
        String allowedOrigin = frontendUrlProperties.getUrl();
        mockMvc.perform(options("/api/v1/buildings/1/reviews")
                        .header(HttpHeaders.ORIGIN, allowedOrigin)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, allowedOrigin))
                .andExpect(header().exists(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS));
    }

    @DisplayName("허용되지 않은 Origin의 CORS Preflight 요청에 대해 403 Forbidden을 반환한다.")
    @Test
    void shouldRejectCorsPreflightFromDisallowedOrigin() throws Exception {
        String disallowedOrigin = "http://malicious-site.com";
        mockMvc.perform(options("/api/v1/buildings/1/reviews")
                        .header(HttpHeaders.ORIGIN, disallowedOrigin)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
