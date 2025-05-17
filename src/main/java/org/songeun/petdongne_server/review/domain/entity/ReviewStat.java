package org.songeun.petdongne_server.review.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.residentialComplex.domain.entity.ResidentialComplex;
import org.songeun.petdongne_server.global.common.BaseEntity;
import org.songeun.petdongne_server.review.domain.error.ReviewErrorStatus;

import static org.songeun.petdongne_server.review.domain.error.ReviewErrorStatus.*;
import static org.songeun.petdongne_server.review.domain.error.ReviewErrorStatus.REVIEW_RESIDENTIAL_COMPLEX_REQUIRED;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewStat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private Integer reviewCount;

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("5.00")
    private Double averageRating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residential_complex_id")
    private ResidentialComplex residentialComplex;

    public static ReviewStat of(Integer reviewCount, Double averageRating, ResidentialComplex residentialComplex) {
        ensureNotNull(reviewCount, NotNullField.REVIEW_COUNT);
        ensureNotNull(averageRating, NotNullField.AVERAGE_RATING);
        ensureNotNull(residentialComplex, NotNullField.RESIDENTIAL_COMPLEX);

        ensureReviewCountIsValid(reviewCount);
        ensureReviewRatingRangeIsValid(averageRating);

        return ReviewStat.builder()
                .reviewCount(reviewCount)
                .averageRating(averageRating)
                .residentialComplex(residentialComplex)
                .build();
    }

    private static <T> void ensureNotNull(T value, NotNullField notNullField) {
        if (value == null) {
            throw new BusinessException(notNullField.errorStatus);
        }
    }

    private static void ensureReviewCountIsValid(Integer reviewCount) {
        if (reviewCount < 0) {
            throw new BusinessException(REVIEW_COUNT_INVALID);
        }
    }

    private static void ensureReviewRatingRangeIsValid(Double averageRating) {
        if (averageRating < 0.0 || averageRating > 5.0) {
            throw new BusinessException(REVIEW_AVERAGE_RATING_REQUIRED);
        }
    }

    @Builder
    private ReviewStat(Integer reviewCount, Double averageRating, ResidentialComplex residentialComplex) {
        this.reviewCount = reviewCount;
        this.averageRating = averageRating;
        this.residentialComplex = residentialComplex;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum NotNullField {
        REVIEW_COUNT("리뷰 수", REVIEW_COUNT_REQUIRED),
        AVERAGE_RATING("평균 평점", REVIEW_RATING_REQUIRED),
        RESIDENTIAL_COMPLEX("주거 단지", REVIEW_RESIDENTIAL_COMPLEX_REQUIRED);

        private final String description;
        private final ReviewErrorStatus errorStatus;
    }

}
