package org.songeun.petdongne_server.review.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.residentialComplex.domain.entity.ResidentialComplex;
import org.songeun.petdongne_server.review.domain.error.ReviewErrorStatus;
import org.songeun.petdongne_server.user.domain.entity.User;
import org.songeun.petdongne_server.global.common.BaseEntity;

import java.time.LocalDate;

import static org.songeun.petdongne_server.review.domain.error.ReviewErrorStatus.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResidenceReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("5.0")
    private Double rating;

    @NotNull
    @Size(min = 50, max = 1000)
    private String reviewText;

    @NotNull
    private Integer lastOccupiedYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residential_complex_id")
    private ResidentialComplex residentialComplex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static ResidenceReview of(Double rating, String reviewText, Integer lastOccupiedYear, ResidentialComplex residentialComplex, User user) {
        ensureNotNull(rating, NotNullField.RATING);
        ensureNotNull(reviewText, NotNullField.REVIEW_TEXT);
        ensureNotNull(lastOccupiedYear, NotNullField.LAST_OCCUPIED_YEAR);
        ensureNotNull(residentialComplex, NotNullField.RESIDENTIAL_COMPLEX);
        ensureNotNull(user, NotNullField.USER);

        ensureReviewTextIsValid(reviewText);
        ensureRatingRangeIsValid(rating);
        ensureOccupiedYearIsNotBeforeApprovalDate(lastOccupiedYear, residentialComplex.approvalDate());

        return ResidenceReview.builder()
                .rating(rating)
                .reviewText(reviewText)
                .lastOccupiedYear(lastOccupiedYear)
                .residentialComplex(residentialComplex)
                .user(user).build();
    }

    /**
     * 리뷰 글자 수, 공백을 검증합니다.
     * @param reviewText 리뷰 내용
     */
    private static void ensureReviewTextIsValid(String reviewText) {
        if (reviewText.trim().isEmpty()) {
            throw new BusinessException(REVIEW_TEXT_REQUIRED);
        }

        if (reviewText.length() < 50 || reviewText.length() >= 1000) {
            throw new BusinessException(REVIEW_TEXT_INVALID_LENGTH);
        }
    }

    private static <T> void ensureNotNull(T value, NotNullField notNullField) {
        if (value == null) {
            throw new BusinessException(notNullField.errorStatus);
        }
    }

    private static void ensureRatingRangeIsValid(Double rating) {
        if (rating <= 0.0 || rating > 5.0) {
            throw new BusinessException(REVIEW_RATING_RANGE_INVALID);
        }
    }

    private static void ensureOccupiedYearIsNotBeforeApprovalDate(Integer lastOccupiedYear, LocalDate approvalDate) {
        if (lastOccupiedYear < approvalDate.getYear()) {
            throw new BusinessException(LAST_OCCUPIED_YEAR_INVALID);
        }
    }

    @Builder
    private ResidenceReview(Double rating, String reviewText, Integer lastOccupiedYear, ResidentialComplex residentialComplex, User user) {
        this.rating = rating;
        this.reviewText = reviewText;
        this.lastOccupiedYear = lastOccupiedYear;
        this.residentialComplex = residentialComplex;
        this.user = user;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum NotNullField{
        RATING("평점", REVIEW_RATING_REQUIRED),
        REVIEW_TEXT("글", REVIEW_TEXT_REQUIRED),
        LAST_OCCUPIED_YEAR("마지막 거주 연도", LAST_OCCUPIED_YEAR_REQUIRED),
        RESIDENTIAL_COMPLEX("거주한 단지", REVIEW_RESIDENTIAL_COMPLEX_REQUIRED),
        USER("리뷰 작성 회원", REVIEW_USER_REQUIRED),

        ;

        private final String description;
        private final ReviewErrorStatus errorStatus;
    }

}
