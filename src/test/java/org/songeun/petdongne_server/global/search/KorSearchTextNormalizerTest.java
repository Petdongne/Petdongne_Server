package org.songeun.petdongne_server.global.search;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KorSearchTextNormalizerTest {

    @Test
    @DisplayName("한글, 숫자를 제외한 문자는 제거한다.")
    void shouldRemainOnlyKorAndNum() {
        //given
        String text = "$$옥수수가 우유 속에#malrannng malraaarrang~$$";

        //when
        String normalized = KorSearchTextNormalizer.normalize(text);

        //then
        assertThat(normalized).isEqualTo("옥수수가 우유 속에 ");
    }

    @Test
    @DisplayName("연속된 공백은 하나의 공백으로 치환한다.")
    void shouldRemainOnlyOneSpace() {
        //given
        String text = "            나 지금 공백에 둘러싸여있어                   ";

        //when
        String normalized = KorSearchTextNormalizer.normalize(text);

        //then
        assertThat(normalized).isEqualTo(" 나 지금 공백에 둘러싸여있어 ");
    }

}