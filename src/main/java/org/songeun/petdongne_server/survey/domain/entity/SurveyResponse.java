package org.songeun.petdongne_server.survey.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.review.domain.entity.ResidenceReview;
import org.songeun.petdongne_server.global.common.BaseEntity;
import org.songeun.petdongne_server.survey.domain.error.SurveyErrorStatus;

import static org.songeun.petdongne_server.survey.domain.error.SurveyErrorStatus.*;
import static org.songeun.petdongne_server.survey.domain.error.SurveyErrorStatus.RESIDENCE_REVIEW_REQUIRED;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyResponse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_optoin_id")
    private SurveyOption surveyOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residence_review_id")
    private ResidenceReview residenceReview;

    public static SurveyResponse of(SurveyOption surveyOption, ResidenceReview residenceReview) {
        ensureNotNull(surveyOption, NotNullField.SURVEY_OPTION);
        ensureNotNull(residenceReview, NotNullField.RESIDENCE_REVIEW);

        return SurveyResponse.builder()
                .surveyOption(surveyOption)
                .residenceReview(residenceReview).build();
    }

    private static <T> void ensureNotNull(T value, NotNullField notNullField) {
        if (value == null) {
            throw new BusinessException(notNullField.errorStatus);
        }
    }

    @Builder
    private SurveyResponse(SurveyOption surveyOption, ResidenceReview residenceReview) {
        this.surveyOption = surveyOption;
        this.residenceReview = residenceReview;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum NotNullField {
        SURVEY_OPTION("설문 옵션", SURVEY_OPTION_REQUIRED),
        RESIDENCE_REVIEW("거주 리뷰", RESIDENCE_REVIEW_REQUIRED);

        private final String description;
        private final SurveyErrorStatus errorStatus;
    }

}
