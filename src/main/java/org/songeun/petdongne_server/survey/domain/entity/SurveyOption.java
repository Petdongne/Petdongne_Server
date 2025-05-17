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
public class SurveyOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Size(min = 1, max = 150)
    private String optionText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_question_id")
    private SurveyQuestion question;

    public String optionText() {
        return this.optionText;
    }

    public static SurveyOption of(String optionText, SurveyQuestion question){
        ensureNotNull(optionText, NotNullField.OPTION_TEXT);
        ensureNotNull(question, NotNullField.QUESTION);
        ensureOptionTextIsValid(optionText);

        return SurveyOption.builder()
                .optionText(optionText)
                .question(question).build();
    }

    private static <T> void ensureNotNull(T value, NotNullField notNullField) {
        if (value == null) {
            throw new BusinessException(notNullField.errorStatus);
        }
    }

    private static void ensureOptionTextIsValid(String optionText) {
        if (optionText.trim().isEmpty()) {
            throw new BusinessException(SURVEY_OPTION_TEXT_EMPTY);
        }
        if (optionText.length() > 150) {
            throw new BusinessException(SURVEY_OPTION_TEXT_TOO_LONG);
        }
    }

    @Builder
    private SurveyOption(String optionText, SurveyQuestion question) {
        this.optionText = optionText;
        this.question = question;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum NotNullField {
        OPTION_TEXT("옵션 내용", SURVEY_OPTION_TEXT_REQUIRED),
        QUESTION("설문 질문", SURVEY_QUESTION_REQUIRED);

        private final String description;
        private final SurveyErrorStatus errorStatus;
    }

}
