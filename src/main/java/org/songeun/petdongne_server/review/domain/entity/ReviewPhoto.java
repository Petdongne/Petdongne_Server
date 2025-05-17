package org.songeun.petdongne_server.review.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.review.domain.error.ReviewErrorStatus;

import static org.songeun.petdongne_server.review.domain.error.ReviewErrorStatus.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String url;

    @NotNull
    private Boolean isPetPhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residence_review_id")
    private ResidenceReview review;

    public static ReviewPhoto of(String url, Boolean isPetPhoto, ResidenceReview review){
        ensureNotNull(url, NotNullField.URL);
        ensureNotNull(isPetPhoto, NotNullField.IS_PET_PHOTO);
        ensureNotNull(review, NotNullField.REVIEW);

        // TODO
//        ensureHasAtLeastOnePetPhoto(review);

        return ReviewPhoto.builder()
                .url(url)
                .isPetPhoto(isPetPhoto)
                .review(review).build();
    }

    private static <T> void ensureNotNull(T value, NotNullField notNullField) {
        if (value == null) {
            throw new BusinessException(notNullField.errorStatus);
        }
    }

    @Builder
    private ReviewPhoto(String url, Boolean isPetPhoto, ResidenceReview review) {
        this.url = url;
        this.isPetPhoto = isPetPhoto;
        this.review = review;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum NotNullField{
        URL("사진 링크", REVIEW_PHOTO_URL_REQUIRED),
        IS_PET_PHOTO("반려동물 사진 여부", REVIEW_PHOTO_TYPE_REQUIRED),
        REVIEW("사진이 첨부될 리뷰", REVIEW_REQUIRED);
        ;

        private final String description;
        private final ReviewErrorStatus errorStatus;
    }

}
