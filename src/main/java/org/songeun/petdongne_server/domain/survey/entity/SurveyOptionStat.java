package org.songeun.petdongne_server.domain.survey.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.domain.residentialComplex.entity.ResidentialComplex;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residential_complex_id")
    private ResidentialComplex residentialComplex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_option_id")
    private SurveyOption surveyOption;

    @Builder
    private SurveyOptionStat(Integer selectedCount, ResidentialComplex residentialComplex, SurveyOption surveyOption) {
        this.selectedCount = selectedCount == null ? 0 : selectedCount;
        this.residentialComplex = residentialComplex;
        this.surveyOption = surveyOption;
    }

}
