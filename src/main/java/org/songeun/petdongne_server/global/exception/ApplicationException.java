package org.songeun.petdongne_server.global.exception;

import lombok.Getter;
import org.songeun.petdongne_server.global.common.ErrorStatus;

@Getter
public class ApplicationException extends RuntimeException{

    private final ErrorStatus status;

    public ApplicationException(ErrorStatus status) {
        super(status.getMessage());
        this.status = status;
    }

}