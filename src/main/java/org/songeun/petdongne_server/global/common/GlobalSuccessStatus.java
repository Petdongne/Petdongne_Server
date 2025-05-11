package org.songeun.petdongne_server.global.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum GlobalSuccessStatus {

    /**
     * COMMON
     */
    OK(HttpStatus.OK,"SUCCESS",  "요청이 성공했습니다."),
    CREATED(HttpStatus.CREATED,"CREATED", "요청이 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
