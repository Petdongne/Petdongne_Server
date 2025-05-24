package org.songeun.petdongne_server.survey.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.residentialComplex.domain.entity.ResidentialComplex;
import org.songeun.petdongne_server.residentialComplex.domain.entity.ResidentialComplexType;
import org.songeun.petdongne_server.review.domain.entity.ResidenceReview;
import org.songeun.petdongne_server.testSupport.GeometryTestUtils;
import org.songeun.petdongne_server.user.domain.entity.ProfileImage;
import org.songeun.petdongne_server.user.domain.entity.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.songeun.petdongne_server.survey.domain.error.SurveyErrorStatus.RESIDENCE_REVIEW_REQUIRED;
import static org.songeun.petdongne_server.survey.domain.error.SurveyErrorStatus.SURVEY_OPTION_REQUIRED;

class SurveyResponseTest {

    @Test
    @DisplayName("설문 옵션과 거주 리뷰로 SurveyResponse 인스턴스를 생성한다.")
    void createSurveyResponse() {
        //given
        SurveyOption surveyOption = createSurveyOption();
        ResidenceReview residenceReview = createResidenceReview();

        //when
        SurveyResponse response = SurveyResponse.of(surveyOption, residenceReview);

        //then
        assertThat(response)
                .extracting("surveyOption", "residenceReview")
                .containsExactly(surveyOption, residenceReview);
    }

    @Test
    @DisplayName("설문 옵션이 NULL이면 예외를 발생시킨다.")
    void fail_createSurveyResponse_whenSurveyOptionIsNull() {
        //given
        SurveyOption surveyOption = null;
        ResidenceReview residenceReview = createResidenceReview();

        //when & then
        assertThatThrownBy(() -> SurveyResponse.of(surveyOption, residenceReview))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SURVEY_OPTION_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("거주 리뷰가 NULL이면 예외를 발생시킨다.")
    void fail_createSurveyResponse_whenResidenceReviewIsNull() {
        //given
        SurveyOption surveyOption = createSurveyOption();
        ResidenceReview residenceReview = null;

        //when & then
        assertThatThrownBy(() -> SurveyResponse.of(surveyOption, residenceReview))
                .isInstanceOf(BusinessException.class)
                .hasMessage(RESIDENCE_REVIEW_REQUIRED.getMessage());
    }

    private SurveyOption createSurveyOption() {
        return SurveyOption.of("비둘기 안내원", createSurveyQuestion());
    }

    private SurveyQuestion createSurveyQuestion() {
        return SurveyQuestion.of("이 아파트의 장점을 선택해주세요.");
    }

    private ResidenceReview createResidenceReview() {
        return ResidenceReview.of(
                4.5,
                "강아지 기르기에 좋은 환경의 아파트입니다. 공원이 가깝고, 배변 봉투가 비치되어 있어서 편리합니다. 비둘기가 가끔 불친절해서 0.5점 뺐어요.",
                2023,
                createResidentialComplex(),
                createUser()
        );
    }

    private ResidentialComplex createResidentialComplex() {
        return ResidentialComplex.of(
                "펫동네 아파트",
                ResidentialComplexType.APARTMENT,
                GeometryTestUtils.defaultPoint(),
                LocalDate.of(2022, 1, 1)
        );
    }

    private User createUser() {
        return User.of("사용자", "user@petdong.com", "password", ProfileImage.of("url"));
    }
}
