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

    RESIDENTIAL_COMPLEX_TYPE_REQUIRED(HttpStatus.BAD_REQUEST, "RESIDENTIAL_COMPLEX_TYPE_REQUIRED", "주거 단지 유형은 NULL일 수 없습니다."),
    RESIDENTIAL_COMPLEX_REQUIRED(HttpStatus.BAD_REQUEST, "RESIDENTIAL_COMPLEX_REQUIRED", "주거 단지는 NULL일 수 없습니다."),
    RESIDENTIAL_COMPLEX_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "RESIDENTIAL_COMPLEX_NAME_REQUIRED", "주거 단지명은 NULL 또는 공백일 수 없습니다."),
    RESIDENTIAL_COMPLEX_LOCATION_REQUIRED(HttpStatus.BAD_REQUEST, "RESIDENTIAL_COMPLEX_LOCATION_REQUIRED", "주거 단지의 위치는 NULL일 수 없습니다."),
    APPROVAL_DATE_IS_FUTURE(HttpStatus.BAD_REQUEST, "APPROVAL_DATE_IS_FUTURE", "사용 승인일은 과거 또는 현재여야합니다."),
    APPROVAL_DATE_IS_REQUIRED(HttpStatus.BAD_REQUEST, "APPROVAL_DATE_IS_REQUIRED", "사용 승인일은 NULL일 수 없습니다."),

    TRANSACTION_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "TRANSACTION_TYPE_NOT_FOUND", "존재하지 않는 거래 유형입니다."),
    TRANSACTION_TYPE_REQUIRED(HttpStatus.BAD_REQUEST, "TRANSACTION_TYPE_REQUIRED", "거래 유형은 NULL일 수 없습니다."),

    TRANSACTION_DATE_IS_FUTURE(HttpStatus.BAD_REQUEST, "TRANSACTION_DATE_IS_FUTURE", "거래 일자는 과거 또는 현재여야 합니다."),
    TRANSACTION_DATE_REQUIRED(HttpStatus.BAD_REQUEST, "TRANSACTION_DATE_REQUIRED", "거래 일자는 NULL일 수 없습니다."),
    TRANSACTION_DATE_BEFORE_APPROVAL_DATE(HttpStatus.BAD_REQUEST, "TRANSACTION_DATE_BEFORE_APPROVE_DATE", "거래 일자는 사용 승인일자와 같거나 이전이어야 합니다."),

    TRANSACTION_PRICE_REQUIRED(HttpStatus.BAD_REQUEST, "TRANSACTION_PRICE_REQUIRED", "거래 금액은 NULL일 수 없습니다."),
    TRANSACTION_PRICE_MUST_BE_POSITIVE(HttpStatus.BAD_REQUEST, "PRICE_MUST_BE_POSITIVE", "거래 가격은 0원을 초과해야 합니다."),

    AREA_REQUIRED(HttpStatus.BAD_REQUEST, "AREA_REQUIRED", "주거 면적은 NULL일 수 없습니다."),
    AREA_VALUE_REQUIRED(HttpStatus.BAD_REQUEST, "AREA_VALUE_REQUIRED", "주거 면적 값은 NULL일 수 없습니다."),
    AREA_VALUE_MUST_BE_POSITIVE(HttpStatus.BAD_REQUEST, "AREA_MUST_BE_POSITIVE", "주거 면적 값은 0보다 커야 합니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
