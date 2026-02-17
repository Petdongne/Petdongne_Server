package org.songeun.petdongne_server.security.authentication;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class BearerAccessToken {

    private static final Pattern bearerPattern =
            Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+)=*$");

    private String value;

    private BearerAccessToken(String value) {
        this.value = value;
    }

    public static Optional<BearerAccessToken> parse(String bearerValue) {
        Matcher matcher = bearerPattern.matcher(bearerValue);
        if (!matcher.matches()) return Optional.empty();
        return Optional.of(new BearerAccessToken(matcher.group("token")));
    }

    public static BearerAccessToken of(String opaqueToken) {
        return new BearerAccessToken(opaqueToken);
    }

    public String getValue() {
        return value;
    }

    public String getValueWithBearer() {
        return "Bearer " + value;
    }
}
