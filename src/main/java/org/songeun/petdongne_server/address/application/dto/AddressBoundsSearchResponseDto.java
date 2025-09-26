package org.songeun.petdongne_server.address.application.dto;

import lombok.Builder;

@Builder
public record AddressBoundsSearchResponseDto(
        String name,
        Double longitude,
        Double latitude,
        String regionLevel
) {

    public static AddressBoundsSearchResponseDto of(
            String name,
            Double longitude,
            Double latitude,
            String regionLevel
    ) {
        return AddressBoundsSearchResponseDto.builder()
                .name(name)
                .longitude(longitude)
                .latitude(latitude)
                .regionLevel(regionLevel).build();
    }

}
