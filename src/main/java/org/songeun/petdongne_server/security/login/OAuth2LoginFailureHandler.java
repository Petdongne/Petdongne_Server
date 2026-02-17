package org.songeun.petdongne_server.security.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public OAuth2LoginFailureHandler(OAuth2LoginRedirectUrls oAuth2LoginRedirectUrls) {
        String redirectUrl = oAuth2LoginRedirectUrls.getFailureRedirectUrl("oauth_failed");
        super.setDefaultFailureUrl(redirectUrl);
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        log.warn("OAuth2 login failed: message={}", exception.getMessage(), exception);
        super.onAuthenticationFailure(request, response, exception);
    }

}
