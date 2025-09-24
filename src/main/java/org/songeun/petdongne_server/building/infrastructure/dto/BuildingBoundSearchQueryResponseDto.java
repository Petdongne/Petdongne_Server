package org.songeun.petdongne_server.building.infrastructure.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.songeun.petdongne_server.global.common.GeoPoint;

@Getter
public class BuildingBoundSearchQueryResponseDto {

    Long id;
    String name;
    Double latitude;
    Double longitude;

    @QueryProjection
    public BuildingBoundSearchQueryResponseDto(Long id, String name, Double longitude, Double latitude) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
