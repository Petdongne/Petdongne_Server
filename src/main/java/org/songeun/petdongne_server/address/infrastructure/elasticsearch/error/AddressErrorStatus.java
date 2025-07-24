package org.songeun.petdongne_server.address.infrastructure.elasticsearch.error;

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

    ADDRESS_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "ADDRESS_TYPE_NOT_FOUND", "존재하지 않는 주소 유형입니다."),
    SIDO_IS_REQUIRED(HttpStatus.BAD_REQUEST, "SIDO_IS_REQUIRED", "시도 레벨의 주소값은 필수입니다."),
    SIGUNGU_IS_REQUIRED_IF_EUPMYEONDONG_EXISTS(HttpStatus.BAD_REQUEST, "SIGUNGU_IS_REQUIRED_IF_EUPMYEONDONG_EXISTS",
            "읍면동 주소값이 존재한다면 시군구 주소값은 필수입니다."),
    MID_ADDRESS_IS_REQUIRED_IF_RE_EXISTS(HttpStatus.BAD_REQUEST, "MID_ADDRESS_IS_REQUIRED_IF_RE_EXISTS",
            "리 주소값이 존재한다면 시군구 및 읍면동 주소값은 필수입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}