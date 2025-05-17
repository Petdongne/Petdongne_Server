package org.songeun.petdongne_server.survey.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.residentialComplex.domain.entity.ResidentialComplex;
import org.songeun.petdongne_server.residentialComplex.domain.entity.ResidentialComplexType;
import org.songeun.petdongne_server.testSupport.GeometryTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.songeun.petdongne_server.survey.domain.error.SurveyErrorStatus.*;

class SurveyOptionStatTest {

    @Test
    @DisplayName("선택 횟수, 주거 단지, 설문 옵션으로 SurveyOptionStat 인스턴스를 생성한다.")
    void createSurveyOptionStat() {
        //given
        Integer selectedCount = 10;
        ResidentialComplex residentialComplex = createResidentialComplex();
        SurveyOption surveyOption = createSurveyOption();

        //when
        SurveyOptionStat stat = SurveyOptionStat.of(selectedCount, residentialComplex, surveyOption);

        //then
        assertThat(stat)
                .extracting("selectedCount", "residentialComplex", "surveyOption")
                .containsExactly(selectedCount, residentialComplex, surveyOption);
    }

    @Test
    @DisplayName("선택 횟수가 NULL이면 예외를 발생시킨다.")
    void fail_createSurveyOptionStat_whenSelectedCountIsNull() {
        //given
        Integer selectedCount = null;
        ResidentialComplex residentialComplex = createResidentialComplex();
        SurveyOption surveyOption = createSurveyOption();

        //when & then
        assertThatThrownBy(() -> SurveyOptionStat.of(selectedCount, residentialComplex, surveyOption))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SURVEY_OPTION_STAT_SELECTED_COUNT_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("주거 단지가 NULL이면 예외를 발생시킨다.")
    void fail_createSurveyOptionStat_whenResidentialComplexIsNull() {
        //given
        Integer selectedCount = 10;
        ResidentialComplex residentialComplex = null;
        SurveyOption surveyOption = createSurveyOption();

        //when & then
        assertThatThrownBy(() -> SurveyOptionStat.of(selectedCount, residentialComplex, surveyOption))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SURVEY_OPTION_STAT_RESIDENTIAL_COMPLEX_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("설문 옵션이 NULL이면 예외를 발생시킨다.")
    void fail_createSurveyOptionStat_whenSurveyOptionIsNull() {
        //given
        Integer selectedCount = 10;
        ResidentialComplex residentialComplex = createResidentialComplex();
        SurveyOption surveyOption = null;

        //when & then
        assertThatThrownBy(() -> SurveyOptionStat.of(selectedCount, residentialComplex, surveyOption))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SURVEY_OPTION_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("선택 횟수가 음수이면 예외를 발생시킨다.")
    void fail_createSurveyOptionStat_whenSelectedCountIsNegative() {
        //given
        Integer selectedCount = -1;
        ResidentialComplex residentialComplex = createResidentialComplex();
        SurveyOption surveyOption = createSurveyOption();

        //when & then
        assertThatThrownBy(() -> SurveyOptionStat.of(selectedCount, residentialComplex, surveyOption))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SURVEY_OPTION_STAT_SELECTED_COUNT_INVALID.getMessage());
    }

    @Test
    @DisplayName("선택 횟수가 0이면 성공적으로 생성된다.")
    void createSurveyOptionStat_whenSelectedCountIsZero() {
        //given
        Integer selectedCount = 0;
        ResidentialComplex residentialComplex = createResidentialComplex();
        SurveyOption surveyOption = createSurveyOption();

        //when
        SurveyOptionStat stat = SurveyOptionStat.of(selectedCount, residentialComplex, surveyOption);

        //then
        assertThat(stat.selectedCount()).isZero();
    }

    private ResidentialComplex createResidentialComplex() {
        return ResidentialComplex.of(
                "오션앤포레스트 뷰 아파트",
                ResidentialComplexType.APARTMENT,
                GeometryTestUtils.defaultPoint(),
                LocalDate.of(2022, 1, 1)
        );
    }

    private SurveyOption createSurveyOption() {
        return SurveyOption.of("비둘기 엘리베이터 안내원", createSurveyQuestion());
    }

    private SurveyQuestion createSurveyQuestion() {
        return SurveyQuestion.of("이 아파트의 장점을 선택해주세요.");
    }

}
