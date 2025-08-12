package org.songeun.petdongne_server.compare.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.songeun.petdongne_server.global.common.ErrorStatus;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AddressBatchErrorStatus implements ErrorStatus {

    TABLE_NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "TABLE_NAME_ALREADY_EXISTS", "이미 존재하는 테이블 이름입니다."),
    TABLE_CREATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "TABLE_CREATION_FAIL", "테이블 생성에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
