package org.songeun.petdongne_server.review.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.residentialComplex.domain.entity.ResidentialComplex;
import org.songeun.petdongne_server.residentialComplex.domain.entity.ResidentialComplexType;
import org.songeun.petdongne_server.testSupport.GeometryTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.songeun.petdongne_server.review.domain.error.ReviewErrorStatus.*;

class ReviewStatTest {

    @Test
    @DisplayName("리뷰 수, 평균 평점, 주거 단지로 ReviewStat 인스턴스를 생성한다.")
    void createReviewStat() {
        //given
        Integer reviewCount = 10;
        Double averageRating = 4.5;
        ResidentialComplex residentialComplex = createResidentialComplex();

        //when
        ReviewStat reviewStat = ReviewStat.of(reviewCount, averageRating, residentialComplex);

        //then
        assertThat(reviewStat)
                .extracting("reviewCount", "averageRating", "residentialComplex")
                .containsExactly(reviewCount, averageRating, residentialComplex);
    }

    @Test
    @DisplayName("리뷰 수가 NULL이면 예외를 발생시킨다.")
    void fail_whenReviewCountIsNull() {
        //given
        Integer invalidReviewCount = null;
        Double averageRating = 4.5;
        ResidentialComplex residentialComplex = createResidentialComplex();

        //when & then
        assertThatThrownBy(() -> ReviewStat.of(invalidReviewCount, averageRating, residentialComplex))
                .isInstanceOf(BusinessException.class)
                .hasMessage(REVIEW_COUNT_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("평균 평점이 NULL이면 예외를 발생시킨다.")
    void fail_whenAverageRatingIsNull() {
        //given
        Integer reviewCount = 10;
        Double invalidAverageRating = null;
        ResidentialComplex residentialComplex = createResidentialComplex();

        //when & then
        assertThatThrownBy(() -> ReviewStat.of(reviewCount, invalidAverageRating, residentialComplex))
                .isInstanceOf(BusinessException.class)
                .hasMessage(REVIEW_RATING_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("주거 단지가 NULL이면 예외를 발생시킨다.")
    void fail_whenResidentialComplexIsNull() {
        //given
        Integer reviewCount = 10;
        Double averageRating = 4.5;
        ResidentialComplex invalidResidentialComplex = null;

        //when & then
        assertThatThrownBy(() -> ReviewStat.of(reviewCount, averageRating, invalidResidentialComplex))
                .isInstanceOf(BusinessException.class)
                .hasMessage(REVIEW_RESIDENTIAL_COMPLEX_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("리뷰 수가 음수이면 예외를 발생시킨다.")
    void fail_whenReviewCountIsNegative() {
        //given
        Integer invalidReviewCount = -1;
        Double averageRating = 4.5;
        ResidentialComplex residentialComplex = createResidentialComplex();

        //when & then
        assertThatThrownBy(() -> ReviewStat.of(invalidReviewCount, averageRating, residentialComplex))
                .isInstanceOf(BusinessException.class)
                .hasMessage(REVIEW_COUNT_INVALID.getMessage());
    }

    @Test
    @DisplayName("평균 평점이 0.0 미만이면 예외를 발생시킨다.")
    void fail_whenAverageRatingIsLessThanZero() {
        //given
        Integer reviewCount = 10;
        Double invalidAverageRating = -0.1;
        ResidentialComplex residentialComplex = createResidentialComplex();

        //when & then
        assertThatThrownBy(() -> ReviewStat.of(reviewCount, invalidAverageRating, residentialComplex))
                .isInstanceOf(BusinessException.class)
                .hasMessage(REVIEW_AVERAGE_RATING_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("평균 평점이 5.0 초과이면 예외를 발생시킨다.")
    void fail_whenAverageRatingIsMoreThanFive() {
        //given
        Integer reviewCount = 10;
        Double invalidAverageRating = 5.1;
        ResidentialComplex residentialComplex = createResidentialComplex();

        //when & then
        assertThatThrownBy(() -> ReviewStat.of(reviewCount, invalidAverageRating, residentialComplex))
                .isInstanceOf(BusinessException.class)
                .hasMessage(REVIEW_AVERAGE_RATING_REQUIRED.getMessage());
    }

    private ResidentialComplex createResidentialComplex() {
        return ResidentialComplex.of(
                "펫동네 아파트",
                ResidentialComplexType.APARTMENT,
                GeometryTestUtils.defaultPoint(),
                LocalDate.of(2022, 1, 1)
        );
    }

}

