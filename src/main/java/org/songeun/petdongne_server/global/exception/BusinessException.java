package org.songeun.petdongne_server.global.exception;

import lombok.Getter;
import org.songeun.petdongne_server.global.common.ErrorStatus;

/**
 * 비즈니스 로직(도메인 규칙) 위반 시 발생하는 예외를 표현합니다.
 */
@Getter
public class BusinessException extends RuntimeException{

    private final ErrorStatus status;

    public BusinessException(ErrorStatus status) {
        super(status.getMessage());
        this.status = status;
    }

    public BusinessException(ErrorStatus status, Throwable cause) {
        super(status.getMessage(), cause);
        this.status = status;
    }

}