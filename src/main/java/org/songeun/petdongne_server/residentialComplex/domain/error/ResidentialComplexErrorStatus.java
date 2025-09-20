package org.songeun.petdongne_server.residentialComplex.domain.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.songeun.petdongne_server.global.common.ErrorStatus;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResidentialComplexErrorStatus implements ErrorStatus {

    TRANSACTION_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "TRANSACTION_TYPE_NOT_FOUND", "존재하지 않는 거래 유형입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
