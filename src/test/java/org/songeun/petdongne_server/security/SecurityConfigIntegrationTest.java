package org.songeun.petdongne_server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.security.session.SessionData;
import org.songeun.petdongne_server.security.session.SessionStore;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.songeun.petdongne_server.user.domain.entity.User;
import org.songeun.petdongne_server.user.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
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
    private StringRedisTemplate stringRedisTemplate;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FrontendUrlProperties frontendUrlProperties;

    @Autowired
    private SessionStore sessionStore;

    private final String VALID_TOKEN = "Bearer valid-test-token";
    private final String INVALID_TOKEN = "invalid-test-token";
    private final Long USER_ID = 1L;
    private User testUser;
    private SessionData testSessionData;
    private String testSessionDataJson;

    @BeforeEach
    void setUp() throws Exception {
        stringRedisTemplate.getConnectionFactory().getConnection().flushAll();

        testUser = User.builder()
                .id(USER_ID)
                .email("test@example.com")
                .nickname("testuser")
                .build();
        testSessionData = new SessionData(USER_ID);
        sessionStore.saveSession(VALID_TOKEN, testSessionData);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(testUser));
    }

    @DisplayName("유효한 토큰으로 보호된 엔드포인트에 접근하면 200 OK를 반환한다.")
    @Test
    void shouldAllowAccessToProtectedEndpointWithValidToken() throws Exception {
        mockMvc.perform(get("/api/v1/buildings/1/reviews")
                        .cookie(new Cookie(SESSION_COOKIE_NAME, VALID_TOKEN)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("유효하지 않은 토큰으로 보호된 엔드포인트에 접근하면 401 Unauthorized를 반환한다.")
    @Test
    void shouldRejectAccessToProtectedEndpointWithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/v1/buildings/1/reviews")
                        .cookie(new Cookie(SESSION_COOKIE_NAME, INVALID_TOKEN)))
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
