package org.songeun.petdongne_server.security.authentication;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class BearerTokenAuthenticationFilter extends AuthenticationFilter {

    public BearerTokenAuthenticationFilter(
            AuthenticationManager opaqueTokenAuthenticationManager,
            AuthenticationConverter bearerTokenAuthenticationConverter,
            @Qualifier("bearerTokenAuthenticationSuccessHandler")
            AuthenticationSuccessHandler bearerTokenAuthenticatoinSuccessHandler) {
        super(opaqueTokenAuthenticationManager, bearerTokenAuthenticationConverter);
        setSuccessHandler(bearerTokenAuthenticatoinSuccessHandler);
    }

}
