package org.songeun.petdongne_server.address.presentation.dto;

import lombok.Builder;

@Builder
public record AddressSearchResponseDto(
        String id,
        String fullAddress
) {

    public static AddressSearchResponseDto of(
            String id,
            String fullAddress
    ) {
        return AddressSearchResponseDto.builder()
                .id(id)
                .fullAddress(fullAddress)
                .build();
    }

}
