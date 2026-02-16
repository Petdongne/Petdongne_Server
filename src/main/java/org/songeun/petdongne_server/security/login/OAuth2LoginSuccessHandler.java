package org.songeun.petdongne_server.security.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.security.session.SessionConfig;
import org.songeun.petdongne_server.security.session.SessionData;
import org.songeun.petdongne_server.security.session.SessionStore;
import org.songeun.petdongne_server.user.domain.entity.AuthenticationProvider;
import org.songeun.petdongne_server.user.domain.entity.User;
import org.songeun.petdongne_server.user.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

import static org.songeun.petdongne_server.security.session.SessionConfig.SESSION_COOKIE_NAME;
import static org.songeun.petdongne_server.security.session.SessionConfig.TIMEOUT_IN_SEC;

@Slf4j
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String BEARER = "Bearer ";
    private final UserRepository userRepository;
    private final OAuth2LoginRedirectUrls oAuth2LoginRedirectUrls;
    private final SessionStore sessionStore;
    private final boolean isCookieSecure;

    public OAuth2LoginSuccessHandler(
            UserRepository userRepository,
            OAuth2LoginRedirectUrls oAuth2LoginRedirectUrls,
            SessionStore sessionStore,
            @Value("${app.cookie.secure}")
            boolean cookieSecure) {

        this.userRepository = userRepository;
        this.oAuth2LoginRedirectUrls = oAuth2LoginRedirectUrls;
        this.sessionStore = sessionStore;
        this.isCookieSecure = cookieSecure;

        setDefaultTargetUrl(oAuth2LoginRedirectUrls.getSuccessRedirectUrl());
        setAlwaysUseDefaultTargetUrl(true);
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        if (!(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
            log.error("Unexpected principal type: {}", authentication.getPrincipal().getClass());
            getRedirectStrategy().sendRedirect(request, response,
                    oAuth2LoginRedirectUrls.getFailureRedirectUrl("invalid_user"));
            return;
        }

        try {
            User user = saveOrUpdateUser(oidcUser);

            String opaqueToken = generateOpaqueToken();

            String sessionId = SessionConfig.SESSION_ID_PREFIX + opaqueToken;
            generateSession(sessionId, user);

            String cookieValue = BEARER + opaqueToken;
            setSessionCookie(response, cookieValue);

            log.info("OAuth2 login successful for user ID: {}", user.getId());

            super.onAuthenticationSuccess(request, response, authentication);

        } catch (Exception e) {
            log.error("OAuth2 login success handler failed", e);
            getRedirectStrategy().sendRedirect(request, response,
                    oAuth2LoginRedirectUrls.getFailureRedirectUrl("server_error"));
        }
    }

    private User saveOrUpdateUser(OidcUser oidcUser) {
        String identifierFromProvider = oidcUser.getSubject();
        String nickname = oidcUser.getNickName();
        String email = oidcUser.getEmail();

        return userRepository
                .findByIdentifierFromProviderAndAuthenticationProvider(
                        identifierFromProvider,
                        AuthenticationProvider.KAKAO
                )
                .map(existingUser -> {
                    existingUser.updateInfo(nickname, email);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User newUser = User.of(nickname, email, identifierFromProvider,
                            AuthenticationProvider.KAKAO, null);
                    return userRepository.save(newUser);
                });
    }

    private String generateOpaqueToken() {
        return UUID.randomUUID().toString() + UUID.randomUUID().toString();
    }

    private void generateSession(String sessionId, User user) throws JsonProcessingException {
        SessionData sessionData = new SessionData(user.getId());
        sessionStore.saveSession(sessionId, sessionData);
    }

    private void setSessionCookie(HttpServletResponse response, String cookieValue) {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, cookieValue);
        cookie.setHttpOnly(true);
        cookie.setSecure(isCookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(TIMEOUT_IN_SEC);

        response.addCookie(cookie);
    }
}