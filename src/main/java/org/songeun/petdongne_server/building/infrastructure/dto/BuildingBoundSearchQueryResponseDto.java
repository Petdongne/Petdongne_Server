package org.songeun.petdongne_server.building.infrastructure.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class BuildingBoundSearchQueryResponseDto {

    Long id;
    String name;
    Double longitude;
    Double latitude;

    @QueryProjection
    public BuildingBoundSearchQueryResponseDto(Long id, String name, Double longitude, Double latitude) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
