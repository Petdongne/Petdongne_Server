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
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_survey_option_stat_option_complex",
                        columnNames = {"survey_option_id", "residential_complex_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyOptionStat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private Integer selectedCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_option_id")
    private SurveyOption surveyOption;

    public Integer selectedCount() {
        return this.selectedCount;
    }


    @Builder
    private SurveyOptionStat(Integer selectedCount, Building building, SurveyOption surveyOption) {
        this.selectedCount = selectedCount == null ? 0 : selectedCount;
        this.building = building;
        this.surveyOption = surveyOption;
    }

}
