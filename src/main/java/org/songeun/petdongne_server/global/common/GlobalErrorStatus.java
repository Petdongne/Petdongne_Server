package org.songeun.petdongne_server.global.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum GlobalErrorStatus implements ErrorStatus {

    /**
     * COMMON
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 요청입니다."),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "MISSING_REQUEST_PARAMETER", "필수 요청 파라미터가 누락되었습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "허용되지 않는 HTTP 메소드입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "금지된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND,"NOT_FOUND", "요청하신 리소스를 찾을 수 없습니다."),
    CONFLICT(HttpStatus.CONFLICT, "CONFLICT", "이미 존재하는 리소스입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버에 오류가 발생했습니다."),

    /**
     * File
     */
    FILE_IS_EMPTY(HttpStatus.BAD_REQUEST, "FILE_IS_EMPTY", "비어있는 파일입니다."),
    MAX_FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "MAX_FILE_SIZE_EXCEEDED","업로드 가능한 최대 파일 용량을 초과하였습니다."),
    INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_FILE_FORMAT", "지원하지 않는 파일 형식입니다."),
    CSV_FILE_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "CSV_FILE_READ_FAILED", "CSV 파일을 읽는데 실패했습니다."),
    UNZIP_PROCESS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "UNZIP_FAILED", "ZIP 파일 압축 해제 중 오류가 발생했습니다. 다시 시도해 주세요."),
    COLLECT_CHILD_FILES_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "COLLECT_CHILD_FILES_FAILED", "디렉터리 내 파일을 수집하는 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
