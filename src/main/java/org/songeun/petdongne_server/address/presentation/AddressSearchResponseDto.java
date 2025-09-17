package org.songeun.petdongne_server.address.presentation;

import lombok.Builder;
import org.songeun.petdongne_server.global.common.GeoPoint;

@Builder
public record AddressSearchResponseDto(
        Long id,
        String entireAddress,
        GeoPoint centerPoint
) {

    public static AddressSearchResponseDto of(Long id, String address, GeoPoint center) {
        return AddressSearchResponseDto.builder()
                .id(id)
                .entireAddress(address)
                .centerPoint(center)
                .build();
    }

}
