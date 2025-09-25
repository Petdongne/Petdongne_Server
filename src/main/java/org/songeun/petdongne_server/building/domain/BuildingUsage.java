package org.songeun.petdongne_server.building.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum BuildingUsage {

    SINGLE_HOUSE_1("단독주택", "01000"),
    SINGLE_HOUSE_2("단독주택", "01001"),
    MULTI_HOUSE("다중주택", "01002"),
    MULTI_FAMILY_HOUSE("다가구 주택", "01003"),
    APARTMENT_COMPLEX("공동주택", "02000"),
    APARTMENT("아파트", "02001"),
    TOWNHOUSE("연립주택", "02002"),
    MULTI_UNIT_HOUSE("다세대 주택", "02003"),
    CONVENIENCE_FACILITY("생활편익시설", "02004"),
    NEIGHBORHOOD_FACILITY_TYPE1("제1종근린생활시설", "03000"),
    OTHER_NEIGHBORHOOD_FACILITY_TYPE1("기타제1종 근린생활시설", "03999"),
    NEIGHBORHOOD_FACILITY_TYPE2("제2종근린생활시설", "04000"),
    VETERINARY_HOSPITAL("동물병원", "04012"),
    OTHER_NEIGHBORHOOD_FACILITY_TYPE2("기타제2종 근린생활시설", "04999"),
    OFFICETEL("오피스텔", "10202"),
    OFFICE_FACILITY("업무시설", "10000"),
    OTHER_GENERAL_OFFICE_FACILITY("기타일반업무시설", "10299");

    private final String value;
    private final String code;

    public static BuildingUsage fromCode(String code) {

        return Arrays.stream(BuildingUsage.values())
                .filter(t-> t.code.equals(code))
                .findAny()
                .orElseThrow(()-> new RuntimeException("엥? 우리 그런애 없어요;;"));
    }

}
