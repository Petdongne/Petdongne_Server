package org.songeun.petdongne_server.address.application.dto;

import lombok.Builder;
import lombok.ToString;

@Builder
public record AddressBoundsSearchResponseDto(
        String name,
        Double longitude,
        Double latitude,
        String regionLevel,
        String geoHash
) {

    public static AddressBoundsSearchResponseDto of(
            String name,
            Double longitude,
            Double latitude,
            String regionLevel,
            String geoHash
    ) {
        return AddressBoundsSearchResponseDto.builder()
                .name(name)
                .longitude(longitude)
                .latitude(latitude)
                .regionLevel(regionLevel)
                .geoHash(geoHash).build();
    }

}
