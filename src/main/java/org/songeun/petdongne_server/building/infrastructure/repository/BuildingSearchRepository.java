package org.songeun.petdongne_server.building.infrastructure.repository;

import org.songeun.petdongne_server.building.infrastructure.dto.BuildingBoundSearchQueryResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BuildingSearchRepository {

    List<BuildingBoundSearchQueryResponseDto> findWithinBounds(Double minLon, Double minLat, Double maxLon, Double maxLat);

}
