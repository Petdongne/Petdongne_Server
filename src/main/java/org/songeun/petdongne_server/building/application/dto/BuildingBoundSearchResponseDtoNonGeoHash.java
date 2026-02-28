package org.songeun.petdongne_server.building.application.dto;

import lombok.Builder;

@Builder
public record BuildingBoundSearchResponseDtoNonGeoHash(
        Long id,
        String name,
        Double longitude,
        Double latitude
) {

    public static BuildingBoundSearchResponseDtoNonGeoHash of(
            final Long id, final String name, final Double longitude, final Double latitude, final String geoHash) {
        return BuildingBoundSearchResponseDtoNonGeoHash.builder()
                .id(id)
                .name(name)
                .longitude(longitude)
                .latitude(latitude).build();
    }

}
