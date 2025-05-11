package org.songeun.petdongne_server.residentialComplex.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

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
                .orElseThrow(()-> new RuntimeException("에엥"));
    }

}
