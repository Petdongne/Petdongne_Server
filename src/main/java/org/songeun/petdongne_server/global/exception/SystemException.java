package org.songeun.petdongne_server.global.exception;

import lombok.Getter;
import org.songeun.petdongne_server.global.common.ErrorStatus;

/**
 * 데이터베이스 연결 실패, 외부 API 오류 등
 * 어플리케이션을 지원하는 시스템(인프라) 장애와 관련된 예외를 표현합니다.
 * <br>
 * 이 예외는 비즈니스 로직이 아닌 시스템 환경의 문제를 나타낼 때 사용합니다.
 */
@Getter
public class SystemException extends RuntimeException{

    private final ErrorStatus status;

    public SystemException(ErrorStatus status) {
        super(status.getMessage());
        this.status = status;
    }

    public SystemException(ErrorStatus status, Throwable cause) {
        super(status.getMessage(), cause);
        this.status = status;
    }

}
