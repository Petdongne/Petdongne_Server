package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.songeun.petdongne_server.global.exception.BusinessException;

import java.util.Arrays;

import static org.songeun.petdongne_server.address.infrastructure.elasticsearch.error.AddressErrorStatus.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AddressType {

    LEGAL_DONG_ADDRESS("법정동"),
    ADMIN_DONG_ADDRESS("행정동");

    private final String koreanName;

    public static AddressType fromName(String name) {
        return Arrays.stream(AddressType.values())
                .filter(type -> type.getKoreanName().equals(name))
                .findAny()
                .orElseThrow(() -> new BusinessException(ADDRESS_TYPE_NOT_FOUND));
    }

    public static AddressType determine(AddressParts addressParts) {
        if (addressParts instanceof LegalDongAddressParts) {
            return LEGAL_DONG_ADDRESS;
        }

        if (addressParts instanceof AdminDongAddressParts){
            return ADMIN_DONG_ADDRESS;
        }

        throw new BusinessException(ADDRESS_TYPE_NOT_FOUND);
    }

}