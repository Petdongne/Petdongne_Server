package org.songeun.petdongne_server.compare.domain;

import lombok.ToString;

import java.util.List;

@ToString
public class ProcessedSearchText {

    private final boolean empty;
    private final TokenType tokenType;
    private final List<String> query;

    public boolean isEmpty() {
        return empty;
    }

    public boolean isSingleCharacter() {
        return tokenType == TokenType.SINGLE_TOKEN_SINGLE_CHAR;
    }

    public String getContentAsString() {
        if (tokenType != TokenType.SINGLE_TOKEN_SINGLE_CHAR) {
            throw new IllegalArgumentException("Only single characters are supported");
        }

        return query.getFirst();
    }

    public List<String> getContent() {
        if (tokenType == TokenType.SINGLE_TOKEN_SINGLE_CHAR) {
            throw new IllegalArgumentException("한 글자 토큰이 아니어야 한다.");
        }

        return query;
    }

    public static ProcessedSearchText ofEmpty() {
        return new ProcessedSearchText(true, null, null);
    }

    public static ProcessedSearchText createForSingleChar(String query) {
        return new ProcessedSearchText(false, List.of(query), TokenType.SINGLE_TOKEN_SINGLE_CHAR);
    }

    public static ProcessedSearchText createForSingleToken(String singleTokenText) {
        return new ProcessedSearchText(false, List.of(singleTokenText), TokenType.SINGLE_TOKEN_MULTI_CHAR);
    }

    public static ProcessedSearchText createForMultiTokens(List<String> query) {
        return new ProcessedSearchText(false, query, TokenType.MULTI_TOKEN);
    }

    private ProcessedSearchText(boolean empty, List<String> query, TokenType tokenType) {
        this.empty = empty;
        this.query = query;
        this.tokenType = tokenType;
    }

    private enum TokenType{
        SINGLE_TOKEN_SINGLE_CHAR,
        SINGLE_TOKEN_MULTI_CHAR,
        MULTI_TOKEN
    }

}
