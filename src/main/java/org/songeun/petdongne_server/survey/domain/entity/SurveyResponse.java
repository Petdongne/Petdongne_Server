package org.songeun.petdongne_server.survey.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.review.domain.entity.ResidenceReview;
import org.songeun.petdongne_server.global.common.BaseEntity;

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

    @Builder
    private SurveyResponse(SurveyOption surveyOption, ResidenceReview residenceReview) {
        this.surveyOption = surveyOption;
        this.residenceReview = residenceReview;
    }

}
