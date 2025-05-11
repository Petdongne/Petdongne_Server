package org.songeun.petdongne_server.global.common;

import org.springframework.http.HttpStatus;

public interface ErrorStatus {

    public HttpStatus getHttpStatus();
    public String getCode();
    public String getMessage();

}
