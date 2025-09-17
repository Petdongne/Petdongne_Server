package org.songeun.petdongne_server.global.search;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderedTokensTest {

    @Test
    @DisplayName("배열로 OrderedTokens 객체를 생성한다.")
    void shouldCreateOrderedTokensFromArray() {
        // given
        String[] input = {"용뭉실", "토끼"};

        // when
        OrderedTokens orderedTokens = OrderedTokens.create(input);

        // then
        assertThat(orderedTokens.getTokens()).hasSize(2);
        assertThat(orderedTokens.getTokens().get(0).getValue()).isEqualTo("용뭉실");
        assertThat(orderedTokens.getTokens().get(1).getValue()).isEqualTo("토끼");
    }

    @Test
    @DisplayName("입력 배열이 null이면 NullPointerException이 발생한다")
    void shouldThrowNPEForNullInput() {
        // when // then
        assertThatThrownBy(() -> OrderedTokens.create(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Tokens cannot be null");
    }

    @Test
    @DisplayName("토큰이 하나이고 길이가 1이면 true를 반환한다")
    void shouldReturnTrueForSingleOneLengthToken() {
        // given
        OrderedTokens orderedTokens = OrderedTokens.create(new String[]{"용"});

        // when
        boolean result = orderedTokens.hasSingleAndOneLenToken();

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("토큰이 여러 개이면 false를 반환한다")
    void shouldReturnFalseForMultipleTokens() {
        // given
        OrderedTokens orderedTokens = OrderedTokens.create(new String[]{"용뭉실", "토끼"});

        // when
        boolean result = orderedTokens.hasSingleAndOneLenToken();

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("토큰이 여러 개 일 때 IllegalStateException을 던진다")
    void shouldThrowWhenToTokenWithMultipleTokens() {
        // given
        OrderedTokens orderedTokens = OrderedTokens.create(new String[]{"용뭉실", "토끼"});

        // when / then
        assertThatThrownBy(orderedTokens::toToken)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("두 개 이상의 토큰을 포함하고 있습니다. 하나의 토큰을 가지고 있는 경우에만 변환할 수 있습니다.");
    }

    @Test
    @DisplayName("토큰이 하나일 때 해당 토큰을 반환한다")
    void shouldReturnSingleToken() {
        // given
        OrderedTokens orderedTokens = OrderedTokens.create(new String[]{"용뭉실"});

        // when
        Token token = orderedTokens.toToken();

        // then
        assertThat(token.getValue()).isEqualTo("용뭉실");
    }

    @Test
    @DisplayName("각 토큰 별 매칭되는 동의어로 치환한다")
    void shouldResolveSynonymsForAllTokens() {
        // given
        OrderedTokens orderedTokens = OrderedTokens.create(new String[]{"실패", "성공"});
        SynonymResolver resolver = value -> value.equals("실패") ? "성공" : value;

        // when
        orderedTokens.resolveSynonym(resolver);

        // then
        assertThat(orderedTokens.getTokens().get(0).getValue()).isEqualTo("성공");
        assertThat(orderedTokens.getTokens().get(1).getValue()).isEqualTo("성공");
    }

    @Test
    @DisplayName("토큰 값을 주어진 구분자로 연결하여 문자열로 반환한다")
    void shouldConcatTokensWithDelimiter() {
        // given
        OrderedTokens orderedTokens = OrderedTokens.create(new String[]{"용뭉실", "살찐", "토끼"});

        // when
        String result = orderedTokens.concatTokensWithDelimiter("-");

        // then
        assertThat(result).isEqualTo("용뭉실-살찐-토끼");
    }

}
