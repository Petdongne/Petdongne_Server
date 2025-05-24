package org.songeun.petdongne_server.survey.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.global.exception.BusinessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.songeun.petdongne_server.survey.domain.error.SurveyErrorStatus.*;

class SurveyOptionTest {

    @Test
    @DisplayName("옵션 내용과 질문으로 설문 옵션 인스턴스를 생성한다.")
    void createSurveyOption() {
        //given
        String optionText = "흰머리가 있는 비둘기가 있습니다.";
        SurveyQuestion question = createSurveyQuestion();

        //when
        SurveyOption surveyOption = SurveyOption.of(optionText, question);

        //then
        assertThat(surveyOption)
                .extracting("optionText", "question")
                .containsExactly(optionText, question);
    }

    @Test
    @DisplayName("옵션 내용이 NULL이면 예외를 발생시킨다.")
    void fail_createSurveyOption_whenOptionTextIsNull() {
        //given
        String optionText = null;
        SurveyQuestion question = createSurveyQuestion();

        //when & then
        assertThatThrownBy(() -> SurveyOption.of(optionText, question))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SURVEY_OPTION_TEXT_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("질문이 NULL이면 예외를 발생시킨다.")
    void fail_createSurveyOption_whenQuestionIsNull() {
        //given
        String optionText = "흰머리가 있는 비둘기가 있습니다.";
        SurveyQuestion question = null;

        //when & then
        assertThatThrownBy(() -> SurveyOption.of(optionText, question))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SURVEY_QUESTION_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("옵션 내용이 공백이면 예외를 발생시킨다.")
    void fail_createSurveyOption_whenOptionTextIsBlank() {
        //given
        String optionText = "   ";
        SurveyQuestion question = createSurveyQuestion();

        //when & then
        assertThatThrownBy(() -> SurveyOption.of(optionText, question))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SURVEY_OPTION_TEXT_EMPTY.getMessage());
    }

    @Test
    @DisplayName("옵션 내용이 150자를 초과하면 예외를 발생시킨다.")
    void fail_createSurveyOption_whenOptionTextExceedsMaxLength() {
        //given
        String optionText = "a".repeat(151);
        SurveyQuestion question = createSurveyQuestion();

        //when & then
        assertThatThrownBy(() -> SurveyOption.of(optionText, question))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SURVEY_OPTION_TEXT_TOO_LONG.getMessage());
    }

    @Test
    @DisplayName("옵션 내용이 정확히 150자이면 성공적으로 생성된다.")
    void createSurveyOption_whenOptionTextIsExactly150() {
        //given
        String optionText = "a".repeat(150);
        SurveyQuestion question = createSurveyQuestion();

        //when
        SurveyOption surveyOption = SurveyOption.of(optionText, question);

        //then
        assertThat(surveyOption.optionText()).hasSize(150);
    }

    private SurveyQuestion createSurveyQuestion() {
        return SurveyQuestion.of("이 아파트의 장점을 선택해주세요.");
    }

}
