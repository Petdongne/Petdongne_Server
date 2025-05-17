package org.songeun.petdongne_server.survey.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.global.exception.BusinessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.songeun.petdongne_server.survey.domain.error.SurveyErrorStatus.*;

class SurveyQuestionTest {

    @Test
    @DisplayName("질문 내용으로 설문 질문 인스턴스를 생성한다.")
    void createSurveyQuestion() {
        //given
        String questionText = "이 아파트에 거주하면서 가장 만족스러운 점은 무엇인가요?";

        //when
        SurveyQuestion surveyQuestion = SurveyQuestion.of(questionText);

        //then
        assertThat(surveyQuestion.questionText()).isEqualTo(questionText);
    }

    @Test
    @DisplayName("질문 내용이 NULL이면 예외를 발생시킨다.")
    void fail_createSurveyQuestion_whenQuestionTextIsNull() {
        //given
        String questionText = null;

        //when & then
        assertThatThrownBy(() -> SurveyQuestion.of(questionText))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SURVEY_QUESTION_TEXT_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("질문 내용이 공백이면 예외를 발생시킨다.")
    void fail_createSurveyQuestion_whenQuestionTextIsBlank() {
        //given
        String questionText = "   ";

        //when & then
        assertThatThrownBy(() -> SurveyQuestion.of(questionText))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SURVEY_QUESTION_TEXT_EMPTY.getMessage());
    }

    @Test
    @DisplayName("질문 내용이 255자를 초과하면 예외를 발생시킨다.")
    void fail_createSurveyQuestion_whenQuestionTextExceedsMaxLength() {
        //given
        String questionText = "a".repeat(256);

        //when & then
        assertThatThrownBy(() -> SurveyQuestion.of(questionText))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SURVEY_QUESTION_TEXT_TOO_LONG.getMessage());
    }

    @Test
    @DisplayName("질문 내용이 정확히 255자이면 성공적으로 생성된다.")
    void createSurveyQuestion_whenQuestionTextIsExactly255() {
        //given
        String questionText = "a".repeat(255);

        //when
        SurveyQuestion surveyQuestion = SurveyQuestion.of(questionText);

        //then
        assertThat(surveyQuestion.questionText()).hasSize(255);
    }

}
