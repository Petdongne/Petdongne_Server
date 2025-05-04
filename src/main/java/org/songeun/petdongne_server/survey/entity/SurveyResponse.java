package org.songeun.petdongne_server.survey.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.review.entity.PetResidenceReview;
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
    @JoinColumn(name = "pet_residence_review_id")
    private PetResidenceReview residenceReview;

    @Builder
    private SurveyResponse(SurveyOption surveyOption, PetResidenceReview residenceReview) {
        this.surveyOption = surveyOption;
        this.residenceReview = residenceReview;
    }

}
