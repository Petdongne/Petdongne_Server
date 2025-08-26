package org.songeun.petdongne_server.global.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StringRegexUtilsTest {

    @Test
    @DisplayName("한글, 숫자, 하나의 공백만 남긴 문자열을 반환한다.")
    void shouldReturnKorAndNumAndSpace(){
        //given
        String text = "나 는 !!! 당도 50 퍼센트 !!!      o 버 블 티 o    를 먹고 싶어";

        //when
        String result = StringRegexUtils.cleanToKorNumSpace(text);

        //then
        assertThat(result).isEqualTo("나 는 당도 50 퍼센트 버 블 티 를 먹고 싶어");
    }

    @Test
    @DisplayName("공백을 기준으로 분할된 문자열을 반환한다.")
    void shouldReturnSplitText(){
        //given
        String text = "1 1";

        //when
        String[] result = StringRegexUtils.splitByWhitespace(text);

        //then
        assertThat(result).containsExactly("1", "1");
    }

}