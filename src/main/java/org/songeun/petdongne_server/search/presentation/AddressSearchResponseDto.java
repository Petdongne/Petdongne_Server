package org.songeun.petdongne_server.search.presentation;

import lombok.Builder;

@Builder
public record AddressSearchResponseDto(
        String fullAddress
) {

    public static AddressSearchResponseDto of(String fullAddress) {
        return AddressSearchResponseDto.builder()
                .fullAddress(fullAddress)
                .build();
    }

}
