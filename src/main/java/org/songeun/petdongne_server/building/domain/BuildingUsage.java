package org.songeun.petdongne_server.building.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum BuildingUsage {

    APARTMENT("아파트", "1"),
    TOWNHOUSE("연립주택", "2"),
    MULTI_UNIT_HOUSE("다세대 주택", "3");

    private final String value;
    private final String code;

    public static BuildingUsage fromCode(String code) {

        return Arrays.stream(BuildingUsage.values())
                .filter(t-> t.code.equals(code))
                .findAny()
                .orElseThrow(()-> new RuntimeException("존재하지 않는 코드입니다. unknown code: " + code));
    }

}
