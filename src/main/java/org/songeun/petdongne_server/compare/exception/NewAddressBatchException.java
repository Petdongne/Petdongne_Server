package org.songeun.petdongne_server.compare.exception;

import org.songeun.petdongne_server.global.common.ErrorStatus;
import org.songeun.petdongne_server.global.exception.BusinessException;

public class NewAddressBatchException extends BusinessException {

  public NewAddressBatchException(AddressBatchErrorStatus status) {
    super(status);
  }

  public NewAddressBatchException(AddressBatchErrorStatus status, Throwable cause) {
    super(status, cause);
  }

}


