package org.songeun.petdongne_server.building.infrastructure.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BuildingGeoHashSearchQueryResponseDto {

    private final Long id;
    private final String name;
    private final Double longitude;
    private final Double latitude;
    private final String geohash;

    @QueryProjection
    public BuildingGeoHashSearchQueryResponseDto(Long id, String name, Double longitude, Double latitude, String geohash) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.geohash = geohash;
    }

}
