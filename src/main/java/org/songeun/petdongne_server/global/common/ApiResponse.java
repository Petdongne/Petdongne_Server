package org.songeun.petdongne_server.global.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "data"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    private final T data;

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return success(GlobalSuccessStatus.OK, data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return success(GlobalSuccessStatus.CREATED, data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(GlobalSuccessStatus status, T data) {
        return ResponseEntity.status(status.getHttpStatus()).body(
                new ApiResponse<>(true, status.getCode(), status.getMessage(), data)
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> fail(ErrorStatus status) {
        return ResponseEntity.status(status.getHttpStatus()).body(
                new ApiResponse<>(false, status.getCode(), status.getMessage(), null)
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> fail(ErrorStatus status, String message) {
        return ResponseEntity.status(status.getHttpStatus()).body(
                new ApiResponse<>(false, status.getCode(), message, null)
        );
    }

}