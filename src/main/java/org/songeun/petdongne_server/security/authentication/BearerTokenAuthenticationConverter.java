package org.songeun.petdongne_server.security.authentication;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.songeun.petdongne_server.security.session.SessionConfig.SESSION_COOKIE_NAME;

@Component
@Slf4j
public class BearerTokenAuthenticationConverter implements AuthenticationConverter {

    private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+)=*$");

    @Override
    public Authentication convert(HttpServletRequest request) {
        String accessToken = getAccessTokenFromCookies(request);
        if (accessToken == null) return null;

        String token = extractValueIfBearer(accessToken);
        if (token == null || token.isEmpty()) {
            return null;
        }

        return new BearerTokenAuthenticationToken(token);
    }

    private String getAccessTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.warn("No cookies found in request");
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> SESSION_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseGet(() -> {
                    log.debug("Session cookie not found");
                    return null;
                });
    }

    private String extractValueIfBearer(String accessToken) {
        Matcher matcher = authorizationPattern.matcher(accessToken);
        if (!matcher.matches()){
            log.debug("Token does not match Bearer pattern: {}", accessToken);
            return null;
        }

        return matcher.group("token");
    }

}
