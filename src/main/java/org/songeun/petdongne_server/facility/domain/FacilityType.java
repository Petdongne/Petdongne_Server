package org.songeun.petdongne_server.facility.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum FacilityType {

    VET("VET", "동물 병원"),
    DOG_PARK("DOG_PARK", "반려견 놀이터"),
    PET_HOTEL("PET_HOTEL", "반려견 호텔");

    private final String code;
    private final String description;

    public static FacilityType fromCode(String code) {

        return Arrays.stream(FacilityType.values())
                .filter(t-> t.code.equals(code))
                .findAny()
                .orElseThrow(()-> new RuntimeException("에엥"));
    }

}
