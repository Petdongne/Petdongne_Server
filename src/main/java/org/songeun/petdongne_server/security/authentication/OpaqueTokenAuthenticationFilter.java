package org.songeun.petdongne_server.security.authentication;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OpaqueTokenAuthenticationFilter extends AuthenticationFilter {

    public OpaqueTokenAuthenticationFilter(
            AuthenticationManager opaqueTokenAuthenticationManager,
            AuthenticationConverter opaqueTokenAuthenticationConverter,
            @Qualifier("opaqueTokenAuthenticationSuccessHandler")
            AuthenticationSuccessHandler opaqueTokenAuthenticationSuccessHandler) {
        super(opaqueTokenAuthenticationManager, opaqueTokenAuthenticationConverter);
        setSuccessHandler(opaqueTokenAuthenticationSuccessHandler);
    }
}
