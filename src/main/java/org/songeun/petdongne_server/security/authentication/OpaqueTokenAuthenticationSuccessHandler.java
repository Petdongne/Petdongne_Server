package org.songeun.petdongne_server.security.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.security.session.SessionStore;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpaqueTokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final SessionStore sessionStore;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        try {
            resetSessionExpiration(authentication);
        } catch (Exception e) {
            log.error("Failed to reset session expiration for user: {}",
                    authentication.getName(), e);
        }
    }

    private void resetSessionExpiration(Authentication authentication) {
        if (!(authentication instanceof OpaqueTokenAuthenticationToken)) {
            log.warn("Unexpected authentication type: {}", authentication.getClass());
            return;
        }

        OpaqueTokenAuthenticationToken opaqueToken = (OpaqueTokenAuthenticationToken) authentication;
        String token = opaqueToken.getValue();
        String sessionId = token;
        sessionStore.resetSessionExpiration(sessionId);
        log.debug("Session expiration reset for token");
    }
}
