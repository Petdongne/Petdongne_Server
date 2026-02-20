package org.songeun.petdongne_server.review.application;

import org.songeun.petdongne_server.security.authentication.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateReviewRequestDto(
        UserPrincipal userId,
        Long buildingId,
        Integer residenceYear,
        Double rating,
        String content,
        List<SurveyAnswerDto> answers,
        List<MultipartFile> photos
) {

}
