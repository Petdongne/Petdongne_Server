package org.songeun.petdongne_server.security.authentication;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.songeun.petdongne_server.security.session.SessionConfig.SESSION_COOKIE_NAME;

@Component
@Slf4j
@RequiredArgsConstructor
public class OpaqueTokenAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        String cookieValue = extractCookieValue(request);
        if (cookieValue == null) return null;

        return new OpaqueTokenUnAuthenticationToken(cookieValue);
    }

    private String extractCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.warn("No cookies found in request");
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> SESSION_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
