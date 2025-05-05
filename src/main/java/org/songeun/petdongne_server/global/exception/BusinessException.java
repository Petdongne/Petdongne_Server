package org.songeun.petdongne_server.global.exception;

import lombok.Getter;
import org.songeun.petdongne_server.global.common.ErrorStatus;

@Getter
public class BusinessException extends RuntimeException{

    private final ErrorStatus status;

    public BusinessException(ErrorStatus status) {
        super(status.getMessage());
        this.status = status;
    }

}