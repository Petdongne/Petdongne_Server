package org.songeun.petdongne_server.review.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.residentialComplex.domain.entity.ResidentialComplex;
import org.songeun.petdongne_server.residentialComplex.domain.entity.ResidentialComplexType;
import org.songeun.petdongne_server.review.domain.error.ReviewErrorStatus;
import org.songeun.petdongne_server.testSupport.GeometryTestUtils;
import org.songeun.petdongne_server.user.domain.entity.ProfileImage;
import org.songeun.petdongne_server.user.domain.entity.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.songeun.petdongne_server.review.domain.error.ReviewErrorStatus.*;

class ReviewPhotoTest {

    @Test
    @DisplayName("사진 URL, 반려동물 사진 여부, 리뷰로 리뷰사진 인스턴스를 생성한다.")
    void createReviewPhoto(){
        //given
        String url = "https://petdongne.com/white-headed/pigeon";
        Boolean isPetPhoto = Boolean.FALSE;
        ResidenceReview review = createReview();

        //when
        ReviewPhoto reviewPhoto = ReviewPhoto.of(url, isPetPhoto, review);

        //then
        assertThat(reviewPhoto)
                .extracting("url", "isPetPhoto", "review")
                .containsExactlyInAnyOrder(url, isPetPhoto, review);
    }

    @Test
    @DisplayName("사진 URL이 NULL이면 예외를 발생시킨다.")
    void fail_createReviewPhoto_whenUrlIsNull(){
        //given
        String invalidUrl = null;
        Boolean isPetPhoto = Boolean.FALSE;
        ResidenceReview review = createReview();

        //when //then
        assertThatThrownBy(() -> ReviewPhoto.of(invalidUrl, isPetPhoto, review))
                .isInstanceOf(BusinessException.class)
                .hasMessage(REVIEW_PHOTO_URL_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("반려동물 사진 여부가 NULL이면 예외를 발생시킨다.")
    void fail_createReviewPhoto_whenIsPetPhotoIsNull(){
        //given
        String url = "https://petdongne.com/white-headed/pigeon";;
        Boolean inValidIsPetPhoto = null;
        ResidenceReview review = createReview();

        //when //then
        assertThatThrownBy(() -> ReviewPhoto.of(url, inValidIsPetPhoto, review))
                .isInstanceOf(BusinessException.class)
                .hasMessage(REVIEW_PHOTO_TYPE_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("리뷰가 NULL이면 예외를 발생시킨다.")
    void fail_createReviewPhoto_whenReviewIsNull(){
        //given
        String url = "https://petdongne.com/white-headed/pigeon";;
        Boolean isPetPhoto = Boolean.FALSE;
        ResidenceReview invalidReview = null;

        //when //then
        assertThatThrownBy(() -> ReviewPhoto.of(url, isPetPhoto, invalidReview))
                .isInstanceOf(BusinessException.class)
                .hasMessage(REVIEW_REQUIRED.getMessage());
    }

    private ResidenceReview createReview() {
        return ResidenceReview.of(5.0, "드 뜨왕 바리앙쥬 메트로 하이-엔드 아파트는 정말 너무 엄청 많이 대단히 완전 뛰어난 아파트예요!", 2022, createResidentialComplex(), createUser());
    }

    private ResidentialComplex createResidentialComplex() {
        return ResidentialComplex.of(
                "왈왈아파트",
                ResidentialComplexType.APARTMENT,
                GeometryTestUtils.defaultPoint(),
                LocalDate.of(2020, 9, 10)
        );
    }

    private User createUser() {
        return User.of("소금빵", "bbangbbang.gmail.com", "dslfkjsl12", ProfileImage.of("url"));
    }

}