package org.songeun.petdongne_server.review.domain.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.songeun.petdongne_server.global.common.ErrorStatus;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReviewErrorStatus implements ErrorStatus {
    REVIEW_TEXT_REQUIRED(HttpStatus.BAD_REQUEST, "REVIEW_TEXT_REQUIRED", "리뷰 내용은 NULL 또는 공백일 수 없습니다."),
    REVIEW_TEXT_INVALID_LENGTH(HttpStatus.BAD_REQUEST, "REVIEW_TEXT_INVALID_LENGTH", "리뷰 내용은 50자 이상 1000자 이하여야 합니다."),

    REVIEW_RATING_RANGE_INVALID(HttpStatus.BAD_REQUEST, "REVIEW_RATING_RANGE_INVALID", "평점은 0.0보다 크고 5.0 이하인 값만 가질 수 있습니다."),
    REVIEW_RATING_REQUIRED(HttpStatus.BAD_REQUEST, "REVIEW_RATING_REQUIRED", "리뷰 평점은 NULL일 수 없습니다."),

    REVIEW_USER_REQUIRED(HttpStatus.BAD_REQUEST, "REVIEW_USER_REQUIRED", "리뷰 작성자는 NULL일 수 없습니다."),
    REVIEW_RESIDENTIAL_COMPLEX_REQUIRED(HttpStatus.BAD_REQUEST, "REVIEW_RESIDENTIAL_COMPLEX_REQUIRED", "리뷰를 추가할 주거단지는 NULL일 수 없습니다."),

    LAST_OCCUPIED_YEAR_REQUIRED(HttpStatus.BAD_REQUEST, "LAST_OCCUPIED_YEAR_REQUIRED", "거주 연도는 NULL일 수 없습니다."),
    LAST_OCCUPIED_YEAR_INVALID(HttpStatus.BAD_REQUEST, "LAST_OCCUPIED_YEAR_INVALID", "거주 연도는 거주한 단지의 사용 승인일의 연도와 같거나 그 이후여야 합니다."),

    REVIEW_PHOTO_URL_REQUIRED(HttpStatus.BAD_REQUEST, "REVIEW_PHOTO_URL_REQUIRED", "리뷰 사진의 URL은 NULL일 수 없습니다."),
    REVIEW_PHOTO_TYPE_REQUIRED(HttpStatus.BAD_REQUEST, "REVIEW_PHOTO_TYPE_REQUIRED", "반려동물 여부는 NULL일 수 없습니다"),

    REVIEW_REQUIRED(HttpStatus.BAD_REQUEST, "REVIEW_REQUIRED", "리뷰는 NULL일 수 없습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
