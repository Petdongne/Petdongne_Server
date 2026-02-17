package org.songeun.petdongne_server.security.session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SessionConfig {

    public static final int SESSION_TIMEOUT_MINUTES = 30;
    public static final String SESSION_COOKIE_NAME = "ACCESS_TOKEN";

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    public boolean isCookieSecure() {
        return cookieSecure;
    }
}
