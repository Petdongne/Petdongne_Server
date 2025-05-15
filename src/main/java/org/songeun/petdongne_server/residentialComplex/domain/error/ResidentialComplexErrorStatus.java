package org.songeun.petdongne_server.residentialComplex.domain.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.songeun.petdongne_server.global.common.ErrorStatus;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResidentialComplexErrorStatus implements ErrorStatus {

    RESIDENTIAL_COMPLEX_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESIDENTIAL_COMPLEX_TYPE_NOT_FOUND", "존재하지 않는 주거 단지 유형입니다."),

    TRANSACTION_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "TRANSACTION_TYPE_NOT_FOUND", "존재하지 않는 거래 유형입니다."),
    TRANSACTION_TYPE_REQUIRED(HttpStatus.BAD_REQUEST, "TRANSACTION_TYPE_REQUIRED", "거래 유형은 NULL일 수 없습니다"),

    TRANSACTION_DATE_IS_AFTER_TODAY(HttpStatus.BAD_REQUEST, "TRANSACTION_DATE_EXCEED_NOW", "거래 날짜는 과거 또는 현재여야 합니다."),
    TRANSACTION_DATE_BEFORE_APPROVE_DATE(HttpStatus.BAD_REQUEST, "TRANSACTION_DATE_BEFORE_APPROVE_DATE", "거래 날짜는 사용 승인일자와 같거나 이전이어야 합니다."),

    AREA_REQUIRED(HttpStatus.BAD_REQUEST, "AREA_REQUIRED", "주거 면적은 NULL일 수 없습니다."),
    PRICE_MUST_BE_POSITIVE(HttpStatus.BAD_REQUEST, "PRICE_MUST_BE_POSITIVE", "거래 가격은 0원을 초과해야 합니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
