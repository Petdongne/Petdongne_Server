package org.songeun.petdongne_server.search.domain.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.songeun.petdongne_server.global.common.ErrorStatus;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AddressErrorStatus implements ErrorStatus {

    ADDRESS_HIERARCHY_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "ADDRESS_HIERARCHY_INVALID_FORMAT", "주소 계층은 숫자 형식이어야 합니다."),
    ADDRESS_HIERARCHY_NOT_FOUND(HttpStatus.NOT_FOUND, "ADDRESS_HIERARCHY_NOT_FOUND", "존재하지 않는 주소 계층입니다."),

    ADDRESS_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "ADDRESS_TYPE_NOT_FOUND", "존재하지 않는 주소 유형입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}