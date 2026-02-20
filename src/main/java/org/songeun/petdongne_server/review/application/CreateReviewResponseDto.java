package org.songeun.petdongne_server.review.application;

import org.songeun.petdongne_server.survey.domain.AnswerOption;
import org.songeun.petdongne_server.survey.domain.QuestionType;

import java.util.List;
import java.util.Map;

public record CreateReviewResponseDto(
        Long reviewId,
        BuildingReviewStatsDto buildingReviewStats,
        List<BuildingQuestionStatDto> buildingQuestionStats
) {

    public record BuildingReviewStatsDto(
            Double averageRating,
            Long totalReviewCount
    ) {}

    public record BuildingQuestionStatDto(
            QuestionType question,
            Map<AnswerOption, Double> answerRatios
    ) {}
}
