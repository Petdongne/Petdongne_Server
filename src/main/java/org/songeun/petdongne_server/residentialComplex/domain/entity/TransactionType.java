package org.songeun.petdongne_server.residentialComplex.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.global.exception.BusinessException;

import java.util.Arrays;

import static org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus.TRANSACTION_TYPE_NOT_FOUND;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TransactionType {

    SALE("SALE", "매매"),
    JEONSE("JEONSE", "전세"),
    MONTHLY_RENT("MONTHLY_RENT", "월세");

    private final String code;
    private final String description;

    public static TransactionType fromCode(String code) {

        return Arrays.stream(TransactionType.values())
                .filter(t-> t.code.equals(code))
                .findAny()
                .orElseThrow(()-> new BusinessException(TRANSACTION_TYPE_NOT_FOUND));
    }

}
