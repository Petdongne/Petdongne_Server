package org.songeun.petdongne_server.testSupport;

import jakarta.servlet.http.Cookie;
import org.songeun.petdongne_server.security.authentication.BearerAccessToken;
import org.songeun.petdongne_server.security.session.SessionData;
import org.songeun.petdongne_server.security.session.SessionStore;
import org.songeun.petdongne_server.user.domain.entity.AuthenticationProvider;
import org.songeun.petdongne_server.user.domain.entity.User;
import org.songeun.petdongne_server.user.infrastructure.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.songeun.petdongne_server.security.session.SessionConfig.SESSION_COOKIE_NAME;

@Profile("test")
@Component
public class AuthTestFixture implements ApplicationRunner {

    private final UserRepository userRepository;
    private final SessionStore sessionStore;

    private User user;

    private final static BearerAccessToken bearerAccessToken =
            BearerAccessToken.parse("Bearer valid-test-token").orElseThrow();

    public AuthTestFixture(UserRepository userRepository, SessionStore sessionStore) {
        this.userRepository = userRepository;
        this.sessionStore = sessionStore;
    }

    /**
     * 테스트 유저와 세션을 seeding 합니다.
     * @param args incoming application arguments
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        long seedUserId = 1L;
        user = User.builder()
                .email("test@example.com")
                .nickname("testuser")
                .identifierFromProvider(UUID.randomUUID().toString())
                .authenticationProvider(AuthenticationProvider.KAKAO)
                .build();

        SessionData sessionData = new SessionData(seedUserId);
        String sessionId = getSessionId();

        sessionStore.saveSession(sessionId, sessionData);
        userRepository.save(user);
    }

    public String getSessionId() {
        return bearerAccessToken.getValue();
    }

    public String resetSession(String sessionId) {
        sessionStore.resetSessionExpiration(sessionId);
        return sessionId;
    }

    public Cookie getSessionCookie(String sessionId) {
        return new Cookie(SESSION_COOKIE_NAME, BearerAccessToken.of(sessionId).getValueWithBearer());
    }

}
