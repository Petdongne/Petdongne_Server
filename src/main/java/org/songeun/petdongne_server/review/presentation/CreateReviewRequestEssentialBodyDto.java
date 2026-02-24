package org.songeun.petdongne_server.review.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.songeun.petdongne_server.review.application.SurveyAnswerDto;

import java.util.List;

public record CreateReviewRequestEssentialBodyDto(
        @NotNull
        Integer residenceYear,

        @NotNull
        String rating,

        @NotBlank
        String content,

        @NotNull
        List<@Valid SurveyAnswerDto> answers

) {
}