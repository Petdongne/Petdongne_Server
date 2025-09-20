package org.songeun.petdongne_server.global.search;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WhiteSpaceTokenizerTest {

    private final WhiteSpaceTokenizer tokenizer = new WhiteSpaceTokenizer();

    @Test
    @DisplayName("문자열을 공백 기준으로 나누어 OrderedTokens를 생성한다")
    void shouldTokenizeTextByWhiteSpace() {
        // given
        String text = "안녕 세상 토큰 테스트";

        // when
        OrderedTokens orderedTokens = tokenizer.tokenize(text);

        // then
        assertThat(orderedTokens.getTokens()).hasSize(4);
        assertThat(orderedTokens.concatTokensWithDelimiter(" ")).isEqualTo("안녕 세상 토큰 테스트");
    }

    @Test
    @DisplayName("연속된 공백은 무시하고 토큰화된다")
    void shouldIgnoreMultipleSpaces() {
        // given
        String text = "토큰   사이   공백";

        // when
        OrderedTokens orderedTokens = tokenizer.tokenize(text);

        // then
        assertThat(orderedTokens.getTokens()).hasSize(3);
        assertThat(orderedTokens.concatTokensWithDelimiter("")).isEqualTo("토큰사이공백");
    }

    @Test
    @DisplayName("공백만 있는 문자열은 빈 토큰 리스트로 처리된다")
    void shouldReturnEmptyTokensForOnlySpaces() {
        // given
        String text = "      ";

        // when
        OrderedTokens orderedTokens = tokenizer.tokenize(text);

        // then
        assertThat(orderedTokens.getTokens()).isEmpty();
    }

    @Test
    @DisplayName("빈 문자열은 빈 토큰 리스트로 처리된다")
    void shouldReturnEmptyTokensForEmptyString() {
        // given
        String text = "";

        // when
        OrderedTokens orderedTokens = tokenizer.tokenize(text);

        // then
        assertThat(orderedTokens.getTokens()).isEmpty();
    }

}
