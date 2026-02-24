package org.songeun.petdongne_server.security.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.security.session.SessionData;
import org.songeun.petdongne_server.security.session.SessionStore;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.songeun.petdongne_server.user.domain.entity.AuthenticationProvider;
import org.songeun.petdongne_server.user.domain.entity.User;
import org.songeun.petdongne_server.user.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.test.context.TestPropertySource;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.songeun.petdongne_server.security.session.SessionConfig.SESSION_COOKIE_NAME;
import static org.songeun.petdongne_server.security.session.SessionConfig.SESSION_TIMEOUT_MINUTES;

@TestPropertySource(properties = {
        "app.frontend.url=http://localhost:3000",
        "app.cookie.secure=false"
})
class OAuth2LoginSuccessHandlerTest extends IntegrationTestSupport {

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SessionStore sessionStore;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        // Clean up database and Redis before each test
        userRepository.deleteAll();
        stringRedisTemplate.getConnectionFactory().getConnection().flushAll();

        // Initialize mock request and response
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Nested
    @DisplayName("신규 사용자 로그인 시나리오")
    class NewUserLogin{

        @Test
        @DisplayName("데이터베이스에 저장한다")
        void shouldSaveNewUser() throws ServletException, IOException {
            // given
            String subject = "kakao";
            String nickname = "뉴비";
            String email = "new-user-email@gmail.com";
            OidcUser oidcUser = createOidcUser(subject, nickname, email);
            Authentication authentication = createAuthentication(oidcUser);

            //when
            oAuth2LoginSuccessHandler.onAuthenticationSuccess(
                    request, response, authentication
            );

            //then
            User savedUser = findUserOrThrow(subject);

            assertThat(savedUser).isNotNull();
            assertThat(savedUser.getNickname()).isEqualTo(nickname);
            assertThat(savedUser.getEmail()).isEqualTo(email);
            assertThat(savedUser.getIdentifierFromProvider()).isEqualTo(subject);
            assertThat(savedUser.getAuthenticationProvider()).isEqualTo(AuthenticationProvider.KAKAO);
        }

        @Test
        @DisplayName("세션을 저장한 후 세션 식별자를 쿠키로 발급한다")
        void shouldSaveSessionToRedis() throws ServletException, IOException {
            // given
            String subject = "kakao";
            String nickname = "뉴비";
            String email = "new-user-email@gmail.com";
            OidcUser oidcUser = createOidcUser(subject, nickname, email);
            Authentication authentication = createAuthentication(oidcUser);

            //when
            oAuth2LoginSuccessHandler.onAuthenticationSuccess(
                    request, response, authentication
            );

            //then
            User savedUser = findUserOrThrow(subject);
            Cookie sessionCookie = response.getCookie(SESSION_COOKIE_NAME);

            ensureValidSessionCookie(sessionCookie);
            ensureValidSessionStored(sessionCookie.getValue(), savedUser);
        }
    }

    private User findUserOrThrow(String subject) {
        return userRepository.findByIdentifierFromProviderAndAuthenticationProvider(
                subject, AuthenticationProvider.KAKAO
        ).orElseThrow();
    }

    @Nested
    @DisplayName("기존 사용자 로그인 시나리오")
    class ExistingUserLogin{

        @Test
        @DisplayName("기존 사용자의 정보를 업데이트한다")
        void shouldUpdateExistingUserInfo() throws Exception {
            // given
            String subject = "kakao-existing";
            String nickname = "옛날닉네임";
            String email = "old@example.com";
            createExistingUser(subject, nickname, email);

            OidcUser oidcUser = createOidcUser(subject, "새로운닉네임", "new@example.com");
            Authentication authentication = createAuthentication(oidcUser);

            // when
            oAuth2LoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

            // then
            User updatedUser = findUserOrThrow(subject);
            assertThat(updatedUser)
                    .extracting("nickname", "email")
                    .containsExactly("새로운닉네임", "new@example.com");
        }

        @Test
        @DisplayName("세션을 저장한 후 세션 식별자를 쿠키로 발급한다")
        void shouldCreateNewSessionForExistingUser() throws Exception {
            // given
            String subject = "kakao";
            String nickname = "옛날닉네임";
            String email = "old@example.com";
            OidcUser oidcUser = createOidcUser(subject, nickname, email);
            Authentication authentication = createAuthentication(oidcUser);

            // when
            oAuth2LoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

            // then
            User savedUser = findUserOrThrow(subject);
            Cookie sessionCookie = response.getCookie(SESSION_COOKIE_NAME);

            ensureValidSessionCookie(sessionCookie);
            ensureValidSessionStored(sessionCookie.getValue(), savedUser);
        }
    }

    private OidcUser createOidcUser(String subject, String nickname, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", subject);
        claims.put("nickname", nickname);
        claims.put("email", email);

        OidcIdToken idToken = new OidcIdToken(
                "token-value",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                claims
        );

        return new DefaultOidcUser(
                Collections.singletonList(new OAuth2UserAuthority(claims)),
                idToken
        );
    }

    private void ensureValidSessionCookie(Cookie sessionCookie) {
        assertThat(sessionCookie).isNotNull();
        assertThat(sessionCookie.getValue()).doesNotContain("Bearer ");
        assertThat(sessionCookie.isHttpOnly()).isTrue();
        assertThat(sessionCookie.getSecure()).isFalse();
        assertThat(sessionCookie.getPath()).isEqualTo("/");
        assertThat(sessionCookie.getMaxAge()).isEqualTo(
                (int) TimeUnit.MINUTES.toSeconds(SESSION_TIMEOUT_MINUTES)
        );
    }

    private void ensureValidSessionStored(String accessToken, User savedUser) throws JsonProcessingException {
        String sessionId = accessToken;
        SessionData session = sessionStore.getSession(sessionId);
        assertThat(session).isNotNull();
        assertThat(session.userId()).isEqualTo(savedUser.getId());
    }

    private Authentication createAuthentication(OidcUser oidcUser) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(oidcUser);
        return authentication;
    }

    private User createExistingUser(String subject, String nickname, String email) {
        User user = User.of(nickname, email, subject, AuthenticationProvider.KAKAO, null);
        return userRepository.save(user);
    }
}