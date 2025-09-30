package org.songeun.petdongne_server.map.application;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.application.dto.AddressBoundsSearchResponseDto;
import org.songeun.petdongne_server.address.application.service.AddressSearchService;
import org.songeun.petdongne_server.building.application.BuildingBoundSearchResponseDto;
import org.songeun.petdongne_server.building.application.BuildingSearchService;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.map.domain.MapErrorStatus;
import org.songeun.petdongne_server.map.domain.ZoomLevel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapSearchService {

    private final AddressSearchService addressSearchService;
    private final BuildingSearchService buildingSearchService;

    public List<AddressBoundsSearchResponseDto> searchClustersWithinBounds(
            Double minLon,Double minLat, Double maxLon, Double maxLat, ZoomLevel zoomLevel) {
        if (!zoomLevel.isSupportedInCluster()) {
            throw new BusinessException(MapErrorStatus.ZOOM_LEVEL_NOT_SUPPORTED);
        }

        return addressSearchService.searchWithinBounds(minLon, minLat, maxLon, maxLat, zoomLevel);
    }

    public List<BuildingBoundSearchResponseDto> searchDetailsWithinBounds(
            Double minLon, Double minLat, Double maxLon, Double maxLat, ZoomLevel zoomLevel) {
        if (!zoomLevel.isSupportedInDetail()) {
            throw new BusinessException(MapErrorStatus.ZOOM_LEVEL_NOT_SUPPORTED);
        }

        return buildingSearchService.searchWithinBounds(minLon, minLat, maxLon, maxLat, zoomLevel);
    }

}
