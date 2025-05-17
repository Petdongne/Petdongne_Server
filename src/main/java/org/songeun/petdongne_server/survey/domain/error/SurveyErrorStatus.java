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

    SURVEY_QUESTION_REQUIRED(HttpStatus.BAD_REQUEST, "SURVEY_QUESTION_REQUIRED", "설문 질문은 NULL일 수 없습니다.");
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
