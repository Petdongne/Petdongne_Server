package org.songeun.petdongne_server.global.search;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderedTokens {

    private List<Token> tokens;

    public static OrderedTokens create(String[] tokens) {
        if (tokens == null) {
            throw new NullPointerException("Tokens cannot be null");
        }

        return new OrderedTokens(Arrays.stream(tokens).map(Token::create).toList());
    }

    public boolean hasSingleAndOneLenToken() {
        if (tokens.size() != 1) {
            return false;
        }

        return tokens.getFirst().isOneLength();
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public Token toToken() {
        if (tokens.size() != 1) {
            throw new IllegalStateException("두 개 이상의 토큰을 포함하고 있습니다. 하나의 토큰을 가지고 있는 경우에만 변환할 수 있습니다.");
        }

        return tokens.getFirst();
    }

    public void resolveSynonym(SynonymResolver synonymResolver) {
        tokens.forEach(token -> token.resolveSynonym(synonymResolver));
    }

    public String concatTokensWithDelimiter(String delimiter) {
        return String.join(delimiter, tokens.stream().map(Token::getValue).toList());
    }

}
