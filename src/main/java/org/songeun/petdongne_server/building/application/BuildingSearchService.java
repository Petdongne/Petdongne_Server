package org.songeun.petdongne_server.building.application;

import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.building.application.dto.BuildingDetailResponseDto;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.building.domain.BuildingGeoHashLengthProvider;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingGeoHashSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.repository.BuildingRepository;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.global.util.GeoHashValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class BuildingSearchService {

    private final GeoHashValidator geoHashValidator;
    private final BuildingGeoHashLengthProvider buildingGeoHashLengthProvider;
    private final LoadingCache<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>> buildingCache;
    private final BuildingRepository buildingRepository;

    public List<BuildingBoundSearchResponseDto> searchWithinBounds(Set<String> geoHashes) {
        geoHashValidator.validGeoHashLength(geoHashes, buildingGeoHashLengthProvider.getGeoHashLength());

        Map<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>>
                buildingCacheAll = buildingCache.getAll(geoHashes);

        return buildingCacheAll.values().stream()
                .flatMap(Optional::stream)
                .flatMap(Set::stream)
                .map(dto -> BuildingBoundSearchResponseDto.of(
                        dto.getId(), dto.getName(), dto.getLongitude(), dto.getLatitude(), dto.getGeohash()))
                .toList();
    }

    public BuildingDetailResponseDto getBuildingDetail(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new BusinessException(GlobalErrorStatus.NOT_FOUND));
        return BuildingDetailResponseDto.from(building);
    }
}
