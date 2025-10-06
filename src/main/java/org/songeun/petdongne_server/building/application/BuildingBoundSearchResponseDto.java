package org.songeun.petdongne_server.building.application;

import lombok.Builder;

@Builder
public record BuildingBoundSearchResponseDto(
        Long id,
        String name,
        Double longitude,
        Double latitude,
        String geoHash
) {

    public static BuildingBoundSearchResponseDto of(
            final Long id, final String name, final Double longitude, final Double latitude, final String geoHash) {
        return BuildingBoundSearchResponseDto.builder()
                .id(id)
                .name(name)
                .longitude(longitude)
                .latitude(latitude)
                .geoHash(geoHash).build();
    }

}
