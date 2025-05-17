package org.songeun.petdongne_server.survey.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.survey.domain.error.SurveyErrorStatus;

import static org.songeun.petdongne_server.survey.domain.error.SurveyErrorStatus.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String questionText;

    public static SurveyQuestion of(String questionText){
        ensureNotNull(questionText, NotNullField.QUESTION_TEXT);
        ensureQuestionTextIsValid(questionText);

        return SurveyQuestion.builder().
                questionText(questionText).build();
    }

    private static <T> void ensureNotNull(T value, NotNullField notNullField) {
        if (value == null) {
            throw new BusinessException(notNullField.errorStatus);
        }
    }

    private static void ensureQuestionTextIsValid(String questionText) {
        if (questionText.trim().isEmpty()) {
            throw new BusinessException(SURVEY_QUESTION_TEXT_EMPTY);
        }

        if (questionText.length() > 255) {
            throw new BusinessException(SURVEY_QUESTION_TEXT_TOO_LONG);
        }

    }

    @Builder
    private SurveyQuestion(String questionText) {
        this.questionText = questionText;
    }

    public String questionText() {
        return questionText;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum NotNullField {
        QUESTION_TEXT("질문 내용", SURVEY_QUESTION_TEXT_REQUIRED);

        private final String description;
        private final SurveyErrorStatus errorStatus;
    }

}
