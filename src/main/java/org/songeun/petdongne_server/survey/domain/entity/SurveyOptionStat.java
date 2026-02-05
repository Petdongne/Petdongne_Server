package org.songeun.petdongne_server.survey.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.global.common.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyOptionStat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private Integer selectedCount;

    @NotNull
    private Integer percentage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "survey_option_id", nullable = false)
    private SurveyOption surveyOption;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "survey_question_id", nullable = false)
    private SurveyQuestion surveyQuestion;

    public Integer selectedCount() {
        return this.selectedCount;
    }

    @Builder
    private SurveyOptionStat(
            Integer selectedCount,
            Building building,
            SurveyOption surveyOption,
            SurveyQuestion surveyQuestion,
            Integer percentage
    ) {
        this.selectedCount = selectedCount;
        this.building = building;
        this.surveyOption = surveyOption;
        this.surveyQuestion = surveyQuestion;
        this.percentage = percentage;
    }

}
