package org.songeun.petdongne_server.survey.domain.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.songeun.petdongne_server.global.common.ErrorStatus;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SurveyErrorStatus implements ErrorStatus {
    SURVEY_QUESTION_TEXT_REQUIRED(HttpStatus.BAD_REQUEST, "SURVEY_QUESTION_TEXT_REQUIRED", "질문 내용은 NULL일 수 없습니다."),
    SURVEY_QUESTION_TEXT_EMPTY(HttpStatus.BAD_REQUEST, "SURVEY_QUESTION_TEXT_EMPTY", "질문 내용은 공백일 수 없습니다."),
    SURVEY_QUESTION_TEXT_TOO_LONG(HttpStatus.BAD_REQUEST, "SURVEY_QUESTION_TEXT_TOO_LONG", "질문 내용은 255자 이하여야 합니다."),

    SURVEY_OPTION_TEXT_REQUIRED(HttpStatus.BAD_REQUEST, "SURVEY_OPTION_TEXT_REQUIRED", "옵션 내용은 NULL일 수 없습니다."),
    SURVEY_OPTION_TEXT_EMPTY(HttpStatus.BAD_REQUEST, "SURVEY_OPTION_TEXT_EMPTY", "옵션 내용은 공백일 수 없습니다."),
    SURVEY_OPTION_TEXT_TOO_LONG(HttpStatus.BAD_REQUEST, "SURVEY_OPTION_TEXT_TOO_LONG", "옵션 내용은 150자 이하여야 합니다."),

    SURVEY_QUESTION_REQUIRED(HttpStatus.BAD_REQUEST, "SURVEY_QUESTION_REQUIRED", "설문 질문은 NULL일 수 없습니다."),

    SURVEY_OPTION_STAT_SELECTED_COUNT_REQUIRED(HttpStatus.BAD_REQUEST, "SURVEY_OPTION_STAT_SELECTED_COUNT_REQUIRED", "선택 횟수는 NULL일 수 없습니다."),
    SURVEY_OPTION_STAT_SELECTED_COUNT_INVALID(HttpStatus.BAD_REQUEST, "SURVEY_OPTION_STAT_SELECTED_COUNT_INVALID", "선택 횟수는 0 이상이어야 합니다."),
    SURVEY_OPTION_STAT_RESIDENTIAL_COMPLEX_REQUIRED(HttpStatus.BAD_REQUEST, "SURVEY_OPTION_STAT_RESIDENTIAL_COMPLEX_REQUIRED", "주거 단지는 NULL일 수 없습니다."),

    SURVEY_OPTION_REQUIRED(HttpStatus.BAD_REQUEST, "SURVEY_OPTION_REQUIRED", "설문 옵션은 NULL일 수 없습니다."),
    RESIDENCE_REVIEW_REQUIRED(HttpStatus.BAD_REQUEST, "", "거주 리뷰는 NULL일 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
