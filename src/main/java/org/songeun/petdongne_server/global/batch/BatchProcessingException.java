package org.songeun.petdongne_server.global.batch;

import org.songeun.petdongne_server.global.common.ErrorStatus;
import org.songeun.petdongne_server.global.exception.BusinessException;

public class BatchProcessingException extends BusinessException {

    public BatchProcessingException(ErrorStatus status) {
        super(status);
    }

    public BatchProcessingException(ErrorStatus status, Throwable cause) {
        super(status, cause);
    }

}
