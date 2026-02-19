package org.songeun.petdongne_server.survey.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.review.domain.ResidenceReview;
import org.songeun.petdongne_server.global.common.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SelectedOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "survey_optoin_id", nullable = false)
    private SurveyOption surveyOption;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "residence_review_id", nullable = false)
    private ResidenceReview residenceReview;

    @Builder
    private SelectedOption(SurveyOption surveyOption, ResidenceReview residenceReview) {
        this.surveyOption = surveyOption;
        this.residenceReview = residenceReview;
    }

}
