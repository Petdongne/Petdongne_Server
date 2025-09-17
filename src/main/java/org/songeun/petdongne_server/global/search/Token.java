package org.songeun.petdongne_server.global.search;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.exception.BusinessException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Token {

    private String value;

    public static Token create(final String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Token value cannot be null or empty");
        }

        return new Token(value);
    }

    public boolean isOneLength() {
        return value.length() == 1;
    }

    public void resolveSynonym(SynonymResolver synonymResolver) {
        this.value = synonymResolver.resolveOrGet(value);
    }

    public String getValue() {
        return value;
    }

}
