package org.songeun.petdongne_server.building.infrastructure.repository;

import org.songeun.petdongne_server.building.infrastructure.dto.BuildingBoundSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingGeoHashSearchQueryResponseDto;

import java.util.List;
import java.util.Set;

public interface BuildingSearchRepository {

    Set<BuildingGeoHashSearchQueryResponseDto> findByGeoHashes(Set<String> geohashes);

}
