package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.songeun.petdongne_server.global.exception.BusinessException;

import java.util.Arrays;

import static org.songeun.petdongne_server.address.infrastructure.elasticsearch.error.AddressErrorStatus.*;

@Getter
@RequiredArgsConstructor
public enum AddressHierarchy {

    SIDO(1, "시도 레벨의 주소입니다. 예시) 충청남도"),
    SIGUNGU(2, "시군구 레벨의 주소입니다. 예시) 충청남도 청양군"),
    EUPMYEONDONG(3, "읍면동 레벨의 주소입니다. 예시) 충청남도 청양군 비봉면"),
    RE(4, "리 레벨의 주소입니다. 예시) 충청남도 청양군 비봉면 사점리");

    private final Integer level;
    private final String description;

    public static AddressHierarchy fromLevel(String level) {
        try {
            return fromLevel(Integer.parseInt(level));
        } catch (NumberFormatException e) {
            throw new BusinessException(ADDRESS_HIERARCHY_INVALID_FORMAT);
        }
    }

    public static AddressHierarchy fromLevel(Integer level) {
        return Arrays.stream(AddressHierarchy.values())
                .filter(hierarchy -> hierarchy.getLevel().equals(level))
                .findAny()
                .orElseThrow(() -> new BusinessException(ADDRESS_HIERARCHY_NOT_FOUND));
    }

    public static AddressHierarchy determine(AddressParts parts) {
        return switch (parts) {
            case AdminDongAddressParts adminParts -> determineAddressHierarchy(adminParts);
            case LegalDongAddressParts legalParts -> determineAddressHierarchy(legalParts);
            case null, default -> {
                log.error("주소 계층을 판별할 수 없는 형식입니다: Unknown type {}", parts);
                throw new BusinessException(AddressErrorStatus.ADDRESS_HIERARCHY_UNSUPPORTED_TYPE);
            }
        };
    }

    private static AddressHierarchy determineAddressHierarchy(LegalDongAddressParts legalParts) {
        if (StringUtils.isNotBlank(legalParts.getSigungu())) {
            if (StringUtils.isNotBlank(legalParts.getEupmyeondong())) {
                if (StringUtils.isNotBlank(legalParts.getRe())) {
                    return RE;
                }
                return EUPMYEONDONG;
            }
            return SIGUNGU;
        }
        return SIDO;
    }

    private static AddressHierarchy determineAddressHierarchy(AdminDongAddressParts adminParts) {
        if (StringUtils.isNotBlank(adminParts.getSigungu())) {
            if (StringUtils.isNotBlank(adminParts.getEupmyeondong())) {
                return EUPMYEONDONG;
            }
            return SIGUNGU;
        }
        return SIDO;
    }

}

