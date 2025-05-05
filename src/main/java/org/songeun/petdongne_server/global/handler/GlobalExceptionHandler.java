package org.songeun.petdongne_server.global.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.songeun.petdongne_server.global.common.ErrorStatus;
import org.songeun.petdongne_server.global.exception.ApplicationException;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 필수 쿼리 파라미터를 누락한 경우 발생하는 error를 handling 합니다.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e
    ) {
        log.warn(">>> handle: MissingServletRequestParameterException", e);

        return ApiResponse.fail(ErrorStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * 지원하지 않는 HTTP method로 요청 시 발생하는 error를 handling합니다.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e
    ) {
        log.warn(">>> handle: HttpRequestMethodNotSupportedException", e);

        return ApiResponse.fail(ErrorStatus.METHOD_NOT_ALLOWED, e.getMessage());
    }

    /**
     * 존재하지 않는 HTTP URI 요청 시 발생하는 error를 handling합니다.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoResourceException(
            NoResourceFoundException e
    ) {
        log.warn(">>> handle: NoResourceException", e);

        return ApiResponse.fail(ErrorStatus.NOT_FOUND, e.getMessage());
    }

    /**
     * BusinessException을 handling 합니다.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(
            BusinessException e
    ) {
        log.warn(">>> handle: BusinessException | " +e.getStatus() + ": "+ e.getMessage());

        return ApiResponse.fail(e.getStatus(), e.getMessage());
    }

    /**
     * ApplicationException을 handling 합니다.
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(
            ApplicationException e
    ) {
        log.warn(">>> handle: ApplicationException | " +e.getStatus() + ": "+ e.getMessage());

        return ApiResponse.fail(e.getStatus(), e.getMessage());
    }

    /**
     * 그 외의 모든 예외를 handling 합니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> unexpectedException(
            Exception e
    ) {
        log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
        log.error("발생 지점: {}", e.getStackTrace()[0]);

        return ApiResponse.fail(ErrorStatus.INTERNAL_SERVER_ERROR);
    }

}
