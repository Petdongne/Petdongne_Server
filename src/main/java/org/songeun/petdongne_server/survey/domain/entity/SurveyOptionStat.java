package org.songeun.petdongne_server.survey.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.residentialComplex.domain.entity.ResidentialComplex;
import org.songeun.petdongne_server.global.common.BaseEntity;
import org.songeun.petdongne_server.survey.domain.error.SurveyErrorStatus;

import static org.songeun.petdongne_server.survey.domain.error.SurveyErrorStatus.SURVEY_OPTION_STAT_SELECTED_COUNT_INVALID;

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

    public Integer selectedCount() {
        return this.selectedCount;
    }

    public static SurveyOptionStat of(Integer selectedCount, ResidentialComplex residentialComplex, SurveyOption surveyOption){
        ensureNotNull(selectedCount, NotNullField.SELECTED_COUNT);
        ensureNotNull(residentialComplex, NotNullField.RESIDENTIAL_COMPLEX);
        ensureNotNull(surveyOption, NotNullField.SURVEY_OPTION);

        ensureSelectedCountIsValid(selectedCount);

        return SurveyOptionStat.builder()
                .selectedCount(selectedCount)
                .residentialComplex(residentialComplex)
                .surveyOption(surveyOption).build();
    }

    private static <T> void ensureNotNull(T value, NotNullField notNullField) {
        if (value == null) {
            throw new BusinessException(notNullField.errorStatus);
        }
    }

    private static void ensureSelectedCountIsValid(Integer selectedCount) {
        if (selectedCount < 0) {
            throw new BusinessException(SURVEY_OPTION_STAT_SELECTED_COUNT_INVALID);
        }
    }

    @Builder
    private SurveyOptionStat(Integer selectedCount, ResidentialComplex residentialComplex, SurveyOption surveyOption) {
        this.selectedCount = selectedCount == null ? 0 : selectedCount;
        this.residentialComplex = residentialComplex;
        this.surveyOption = surveyOption;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum NotNullField {
        SELECTED_COUNT("선택 횟수", SurveyErrorStatus.SURVEY_OPTION_STAT_SELECTED_COUNT_REQUIRED),
        RESIDENTIAL_COMPLEX("주거 단지", SurveyErrorStatus.SURVEY_OPTION_STAT_RESIDENTIAL_COMPLEX_REQUIRED),
        SURVEY_OPTION("설문 옵션", SurveyErrorStatus.SURVEY_OPTION_REQUIRED);

        private final String description;
        private final SurveyErrorStatus errorStatus;
    }

}
