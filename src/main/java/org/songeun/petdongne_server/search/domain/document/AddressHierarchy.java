package org.songeun.petdongne_server.search.domain.document;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.global.exception.BusinessException;

import java.util.Arrays;

import static org.songeun.petdongne_server.search.domain.error.AddressErrorStatus.ADDRESS_HIERARCHY_INVALID_FORMAT;
import static org.songeun.petdongne_server.search.domain.error.AddressErrorStatus.ADDRESS_HIERARCHY_NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum AddressHierarchy {

    SIDO(1, "시도 레벨의 주소입니다. 예시) 충청남도"),
    SIGUNGU(2, "시군구 레벨의 주소입니다. 예시) 충청남도 청양군"),
    EUPMYEONDONG(3, "읍면동 레벨의 주소입니다. 예시) 충청남도 청양군 비봉면"),
    RE(4, "리 레벨의 주소입니다. 예시) 충청남도 청양군 비봉면 사점리");

    private final Integer level;
    private final String description;

    public static AddressHierarchy fromLevel(String code) {
        try {
            return fromLevel(Integer.parseInt(code));
        } catch (NumberFormatException e) {
            throw new BusinessException(ADDRESS_HIERARCHY_INVALID_FORMAT);
        }
    }

    public static AddressHierarchy fromLevel(Integer code) {
        return Arrays.stream(AddressHierarchy.values())
                .filter(hierarchy -> hierarchy.getLevel().equals(code))
                .findAny()
                .orElseThrow(() -> new BusinessException(ADDRESS_HIERARCHY_NOT_FOUND));
    }

}