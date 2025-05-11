package org.songeun.petdongne_server.residentialComplex.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResidentialComplexType {

    OFFICETEL("OFFICETEL", "오피스텔"),
    APARTMENT("APARTMENT", "아파트"),
    VILLA("VILLA", "빌라"),
    STUDIO("STUDIO", "원룸");

    private final String code;
    private final String description;

    public static ResidentialComplexType fromCode(String code) {

        return Arrays.stream(ResidentialComplexType.values())
                .filter(t-> t.code.equals(code))
                .findAny()
                .orElseThrow(()-> new RuntimeException("에엥"));
    }

}
