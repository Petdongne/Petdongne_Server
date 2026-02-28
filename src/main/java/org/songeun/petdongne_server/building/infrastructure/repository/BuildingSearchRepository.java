package org.songeun.petdongne_server.building.infrastructure.repository;

import org.songeun.petdongne_server.building.application.dto.BuildingBoundSearchResponseDtoNonGeoHash;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingBoundSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingGeoHashSearchQueryResponseDto;

import java.util.List;
import java.util.Set;

public interface BuildingSearchRepository {

    Set<BuildingGeoHashSearchQueryResponseDto> findByGeoHashes(Set<String> geohashes);

    List<BuildingBoundSearchQueryResponseDto> findByBBox(double minLat, double minLng, double maxLat, double maxLng);
}
