package org.songeun.petdongne_server.security.session;

public class SessionConfig {

    public static final String SESSION_ID_PREFIX = "session:";
    public static final String SESSION_COOKIE_NAME = "ACCESS_TOKEN";
    public static final int SESSION_TIMEOUT_MINUTES = 30;
    public static final int TIMEOUT_IN_SEC = SESSION_TIMEOUT_MINUTES * 60;

}
