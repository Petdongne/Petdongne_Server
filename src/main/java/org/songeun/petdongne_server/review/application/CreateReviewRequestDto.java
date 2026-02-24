package org.songeun.petdongne_server.review.application;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.songeun.petdongne_server.global.common.Image;
import org.songeun.petdongne_server.review.domain.Rating;
import org.songeun.petdongne_server.security.authentication.UserPrincipal;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

public record CreateReviewRequestDto(
        @NotNull
        UserPrincipal principal,
        long buildingId,
        int residenceYear,
        @NotNull
        Rating rating,
        @NotEmpty
        String content,
        @NotNull
        List<SurveyAnswerDto> answers,
        @Nullable
        List<Image> images
) {

}
