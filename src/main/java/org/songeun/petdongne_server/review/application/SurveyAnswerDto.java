package org.songeun.petdongne_server.review.application;

import jakarta.validation.constraints.NotNull;
import org.songeun.petdongne_server.survey.domain.AnswerOption;
import org.songeun.petdongne_server.survey.domain.QuestionType;

public record SurveyAnswerDto(
        @NotNull
        QuestionType question,

        @NotNull
        AnswerOption answer
) {
}
