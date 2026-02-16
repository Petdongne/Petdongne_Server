package org.songeun.petdongne_server.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class OpaqueTokenAuthenticationToken extends AbstractAuthenticationToken {

    private final UserPrincipal principal;
    private final String token;

    public OpaqueTokenAuthenticationToken(
            String token,
            UserPrincipal userPrincipal,
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        setAuthenticated(true);
        this.token = token;
        this.principal = userPrincipal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getToken() {
        return token;
    }

}
