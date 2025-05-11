package org.songeun.petdongne_server.facility.domain.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.songeun.petdongne_server.global.common.ErrorStatus;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FacilityErrorStatus implements ErrorStatus {

    FACILITY_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "FACILITY_TYPE_NOT_FOUND", "존재하지 않는 시설 유형입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
