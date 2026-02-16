package org.songeun.petdongne_server.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class BearerTokenAuthenticationToken extends AbstractAuthenticationToken {

    private String token;

    public BearerTokenAuthenticationToken(String token) {
        super(Collections.emptyList());
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return this.token;
    }
}

