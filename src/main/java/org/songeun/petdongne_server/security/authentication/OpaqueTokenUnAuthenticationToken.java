package org.songeun.petdongne_server.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class OpaqueTokenUnAuthenticationToken extends AbstractAuthenticationToken {

    private String token;

    public OpaqueTokenUnAuthenticationToken(String token) {
        super(Collections.emptyList());
        this.token = token;
    }

    public String getValue() {
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

