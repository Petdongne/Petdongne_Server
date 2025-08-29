package org.songeun.petdongne_server.compare.presentation.dto;

import lombok.Builder;

@Builder
public record AddressSearchResponseDto(
        Long id,
        String address
) {

    public static AddressSearchResponseDto of(Long id, String address) {
        return AddressSearchResponseDto.builder()
                .id(id)
                .address(address)
                .build();
    }

}
