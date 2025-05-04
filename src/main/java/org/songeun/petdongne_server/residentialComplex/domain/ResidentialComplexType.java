package org.songeun.petdongne_server.residentialComplex.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
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
