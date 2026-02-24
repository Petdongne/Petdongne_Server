package org.songeun.petdongne_server.global.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.songeun.petdongne_server.global.common.FieldErrorResponse;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.exception.SystemException;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 필수 쿼리 파라미터를 누락한 경우 발생하는 error를 handling 합니다.
     */
    @ExceptionHandler(value = {MissingServletRequestParameterException.class, MissingServletRequestPartException.class})
    public ResponseEntity<ApiResponse<Object>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e
    ) {
        log.warn(">>> handle: MissingServletRequestParameterException", e);

        String field = e.getParameterName();
        String message = String.format("Request param or part '%s'는 필수입니다.", field);

        var error = new FieldErrorResponse(field, message);

        return ApiResponse.failWithDetails(GlobalErrorStatus.MISSING_REQUEST_PARAMETER, error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        List message;
        if (!exception.getBindingResult().getFieldErrors().isEmpty()) {
            message = exception.getBindingResult().getFieldErrors().stream()
                    .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
                    .toList();
        } else {
            message = List.of(GlobalErrorStatus.BAD_REQUEST.getMessage());
        }
        return ApiResponse.failWithDetails(GlobalErrorStatus.BAD_REQUEST, message);
    }

    /**
     * 제약 조건 위반 시 발생하는 error를 handling합니다.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(
            ConstraintViolationException e
    ) {
        log.warn(">>> handle: ConstraintViolationException", e);

        var errors = e.getConstraintViolations().stream()
                .map(violation -> {
                    String field = extractFieldName(violation.getPropertyPath().toString());
                    String message = violation.getMessage();
                    return new FieldErrorResponse(field, message);
                })
                .toList();

        return ApiResponse.failWithDetails(GlobalErrorStatus.BAD_REQUEST, errors);
    }

    /**
     * 지원하지 않는 HTTP method로 요청 시 발생하는 error를 handling합니다.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e
    ) {
        log.warn(">>> handle: HttpRequestMethodNotSupportedException", e);

        return ApiResponse.fail(GlobalErrorStatus.METHOD_NOT_ALLOWED, e.getMessage());
    }

    /**
     * 존재하지 않는 HTTP URI 요청 시 발생하는 error를 handling합니다.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoResourceException(
            NoResourceFoundException e
    ) {
        log.warn(">>> handle: NoResourceException", e);

        return ApiResponse.fail(GlobalErrorStatus.NOT_FOUND, e.getMessage());
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
     * SystemException을 handling 합니다.
     */
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(
            SystemException e
    ) {
        log.warn(">>> handle: SystemException | " +e.getStatus() + ": "+ e.getMessage());

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

        return ApiResponse.fail(GlobalErrorStatus.INTERNAL_SERVER_ERROR);
    }

    private String extractFieldName(String propertyPath) {
        String[] parts = propertyPath.split("\\.");
        return parts.length > 0 ? parts[parts.length - 1] : propertyPath;
    }

}
