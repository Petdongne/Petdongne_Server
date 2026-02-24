package org.songeun.petdongne_server.review.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.review.application.SurveyAnswerDto;
import org.songeun.petdongne_server.survey.domain.QuestionType;
import org.songeun.petdongne_server.user.domain.entity.User;
import org.songeun.petdongne_server.global.common.BaseEntity;

import java.time.Year;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// todo (유저+빌딩) 유니크 제약 조건
@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResidenceReview extends BaseEntity {

    private static final int MIN_CONTENT_LENGTH = 50;
    private static final int MAX_CONTENT_LENGTH = 1000;
    private static final int MAX_REVIEW_IMAGE_COUNT = 3;
    private static final int SURVEY_IN_REVIEW_COUNT = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Convert(converter = RatingAttributeConverter.class)
    @Column(name = "rating", nullable = false)
    private Rating rating;

    @NotNull
    @Size(min = MIN_CONTENT_LENGTH, max = MAX_CONTENT_LENGTH)
    private String reviewText;

    @NotNull
    private Integer lastOccupiedYear;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    private ResidenceReview(Rating rating, String reviewText, Integer lastOccupiedYear, Building building, User user) {
        this.rating = rating;
        this.reviewText = reviewText;
        this.lastOccupiedYear = lastOccupiedYear;
        this.building = building;
        this.user = user;
    }

    public static ResidenceReview create(
            Rating rating, String reviewText, int lastOccupiedYear, Building building, User user) {
        validateReviewText(reviewText);
        validateLastOccupiedYear(lastOccupiedYear);
        return new ResidenceReview(rating, reviewText, lastOccupiedYear, building, user);
    }

    private static void validateReviewText(String reviewText) {
        if (reviewText == null || reviewText.isBlank()) {
            log.error("Review text is null or empty");
            throw new BusinessException(GlobalErrorStatus.BAD_REQUEST);
        }
        if (reviewText.length() < MIN_CONTENT_LENGTH || reviewText.length() > MAX_CONTENT_LENGTH) {
            throw new BusinessException(GlobalErrorStatus.BAD_REQUEST,
                    "리뷰 내용은 " + MIN_CONTENT_LENGTH +"자 에서 " + MAX_CONTENT_LENGTH + "자 사이여야 합니다");
        }
    }

    private static void validateLastOccupiedYear(Integer lastOccupiedYear) {
        int currentYear = Year.now().getValue();
        if (lastOccupiedYear < 1000 || lastOccupiedYear > 9999 || lastOccupiedYear > currentYear) {
            log.error("Last occupied year {} is invalid value", lastOccupiedYear);
            throw new BusinessException(GlobalErrorStatus.BAD_REQUEST);
        }
    }

    public static void validateImgCount(int count) {
        if (count > MAX_REVIEW_IMAGE_COUNT) {
            throw new BusinessException(GlobalErrorStatus.BAD_REQUEST,
                    "이미지 파일은 최대 " +  MAX_REVIEW_IMAGE_COUNT + "개까지 업로드할 수 있습니다.");
        }
    }

    public static void validateInvalidSurveyAnswers(List<SurveyAnswerDto> answers){
        Set<QuestionType> uniqueQuestions = answers.stream()
                .map(SurveyAnswerDto::question)
                .collect(Collectors.toSet());

        if (uniqueQuestions.size() != SURVEY_IN_REVIEW_COUNT) {
            throw new BusinessException(GlobalErrorStatus.BAD_REQUEST,
                    "응답이 누락되거나 중복된 설문조사가 있습니다.");
        }
    }

    public static int getMaxReviewImageCount() {
        return MAX_REVIEW_IMAGE_COUNT;
    }
}
