package org.songeun.petdongne_server.address.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.songeun.petdongne_server.global.common.ErrorStatus;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AddressErrorStatus implements ErrorStatus {

    SEARCH_TEXT_NULL_OR_EMPTY_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "SEARCH_TEXT_NULL_OR_EMPTY_NOT_ALLOWED", "검색어는 반드시 값을 포함해야합니다."),
    SIDO_NULL_OR_EMPTY_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "SIDO_NULL_OR_EMPTY_NOT_ALLOWED", "시도 레벨의 주소는 반드시 값을 포함해야합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
