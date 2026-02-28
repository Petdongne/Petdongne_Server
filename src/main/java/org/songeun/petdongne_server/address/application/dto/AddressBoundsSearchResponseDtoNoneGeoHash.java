package org.songeun.petdongne_server.address.application.dto;

import lombok.Builder;

@Builder
public record AddressBoundsSearchResponseDtoNoneGeoHash(
        String name,
        Double longitude,
        Double latitude,
        String regionLevel
) {

    public static AddressBoundsSearchResponseDtoNoneGeoHash of(
            String name,
            Double longitude,
            Double latitude,
            String regionLevel
    ) {
        return AddressBoundsSearchResponseDtoNoneGeoHash.builder()
                .name(name)
                .longitude(longitude)
                .latitude(latitude)
                .regionLevel(regionLevel).build();
    }

}
