package org.songeun.petdongne_server.residentialComplex.domain.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.songeun.petdongne_server.global.common.ErrorStatus;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResidentialComplexErrorStatus implements ErrorStatus {

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
