package org.songeun.petdongne_server.compare.infrastructure.batch.exception;

import org.songeun.petdongne_server.global.exception.BusinessException;

public class NewAddressBatchException extends BusinessException {

  public NewAddressBatchException(AddressBatchErrorStatus status) {
    super(status);
  }

  public NewAddressBatchException(AddressBatchErrorStatus status, Throwable cause) {
    super(status, cause);
  }

}


