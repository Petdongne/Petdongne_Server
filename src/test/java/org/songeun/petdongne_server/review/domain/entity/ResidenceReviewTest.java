package org.songeun.petdongne_server.review.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.residentialComplex.domain.entity.ResidentialComplex;
import org.songeun.petdongne_server.residentialComplex.domain.entity.ResidentialComplexType;
import org.songeun.petdongne_server.testSupport.GeometryTestUtils;
import org.songeun.petdongne_server.user.domain.entity.ProfileImage;
import org.songeun.petdongne_server.user.domain.entity.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.songeun.petdongne_server.review.domain.error.ReviewErrorStatus.*;

class ResidenceReviewTest {

    @Test
    @DisplayName("평점, 글, 마지막 거주 연도, 거주 단지, 회원으로 리뷰 인스턴스를 생성한다.")
    void createReview(){
        //given
        Double rating = 5.0;
        String reviewText = createReviewText();
        int lastOccupiedYear = 2020;
        ResidentialComplex residentialComplex = createResidentialComplex(LocalDate.of(2020, 9, 10));
        User user = createUser();

        //when
        ResidenceReview review = ResidenceReview.of(rating, reviewText, lastOccupiedYear, residentialComplex, user);

        //then
        assertThat(review)
                .extracting("rating", "reviewText", "lastOccupiedYear", "residentialComplex")
                .containsExactly(rating, reviewText, lastOccupiedYear, residentialComplex);

    }

    @Test
    @DisplayName("마지막 거주 연도가 거주한 단지의 사용 승인일 이전이면 예외를 발생시킨다.")
    void fail_createReview_whenOccupiedYearIsBeforeApprovalDate(){
        //given
        Double rating = 5.0;
        String reviewText = createReviewText();
        int invalidLastOccupiedYear = 2019;
        ResidentialComplex residentialComplex = createResidentialComplex(LocalDate.of(2020, 9, 10));
        User user = createUser();

        //when //then
        assertThatThrownBy(() -> ResidenceReview.of(rating, reviewText, invalidLastOccupiedYear, residentialComplex, user))
                .isInstanceOf(BusinessException.class)
                .hasMessage(LAST_OCCUPIED_YEAR_INVALID.getMessage());
    }

    @Test
    @DisplayName("리뷰 글자 수가 50자 미만이면 예외를 발생시킨다.")
    void fail_createReview_whenTextIsInvalid(){
        //given
        Double rating = 5.0;
        String reviewText = createInvalidReviewText();
        int lastOccupiedYear = 2020;
        ResidentialComplex residentialComplex = createResidentialComplex(LocalDate.of(2020, 9, 10));
        User user = createUser();

        //when //then
        assertThatThrownBy(() -> ResidenceReview.of(rating, reviewText, lastOccupiedYear, residentialComplex, user))
                .isInstanceOf(BusinessException.class)
                .hasMessage(REVIEW_TEXT_INVALID_LENGTH.getMessage());
    }

    @Test
    @DisplayName("리뷰 평점이 0.0 이하이면 예외를 발생시킨다.")
    void fail_createReview_whenRatingIsZeroOrLess(){
        //given
        Double invalidRating = 0.0;
        String reviewText = createReviewText();
        int lastOccupiedYear = 2020;
        ResidentialComplex residentialComplex = createResidentialComplex(LocalDate.of(2020, 9, 10));
        User user = createUser();

        //when //then
        assertThatThrownBy(() -> ResidenceReview.of(invalidRating, reviewText, lastOccupiedYear, residentialComplex, user))
                .isInstanceOf(BusinessException.class)
                .hasMessage(REVIEW_RATING_RANGE_INVALID.getMessage());
    }

    @Test
    @DisplayName("리뷰 평점이 5.0을 초과하면 예외를 발생시킨다.")
    void fail_createReview_whenRatingIsAboveFive(){
        //given
        Double invalidRating = 5.1;
        String reviewText = createReviewText();
        int lastOccupiedYear = 2020;
        ResidentialComplex residentialComplex = createResidentialComplex(LocalDate.of(2020, 9, 10));
        User user = createUser();

        //when //then
        assertThatThrownBy(() -> ResidenceReview.of(invalidRating, reviewText, lastOccupiedYear, residentialComplex, user))
                .isInstanceOf(BusinessException.class)
                .hasMessage(REVIEW_RATING_RANGE_INVALID.getMessage());
    }

    private String createInvalidReviewText() {
        return "조용하고 교통이 편리하며 주변 편의시설도 좋아 만족합니다.";
    }

    private String createReviewText() {
        return "이 아파트는 조용한 주거 환경과 편리한 교통 접근성을 모두 갖추고 있어 매우 만족스럽습니다. 인근에 공원과 마트도 가까워 생활이 편리하며, 단지 내 청결 관리도 잘 되어 있어 쾌적하게 지낼 수 있었습니다.";
    }

    private ResidentialComplex createResidentialComplex(LocalDate approvalDate) {
        return ResidentialComplex.of(
                "왈왈아파트",
                ResidentialComplexType.APARTMENT,
                GeometryTestUtils.defaultPoint(),
                approvalDate
        );
    }

    private User createUser() {
        return User.of("소금빵", "bbangbbang.gmail.com", "dslfkjsl12", ProfileImage.of("url"));
    }

}