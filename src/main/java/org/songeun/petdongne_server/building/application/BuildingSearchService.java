package org.songeun.petdongne_server.building.application;

import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingGeoHashSearchQueryResponseDto;
import org.songeun.petdongne_server.global.util.GeoHashUtil;
import org.songeun.petdongne_server.map.domain.ZoomLevel;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class BuildingSearchService {

    public static final int BUILDING_GEOHASH_LENGTH = 6;
    private final LoadingCache<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>> buildingCache;

    public List<BuildingBoundSearchResponseDto> searchWithinBounds(
            Double minLon, Double minLat, Double maxLon, Double maxLat, ZoomLevel zoomLevel) {
        Assert.notNull(maxLat, "maxLat must not be null");
        Assert.notNull(maxLon, "maxLon must not be null");
        Assert.notNull(minLat, "minLat must not be null");
        Assert.notNull(minLon, "minLon must not be null");

        validateBoundsRange(minLon, minLat, maxLon, maxLat);
        validateMaxGreaterThanMin(minLon, minLat, maxLon, maxLat);

        Set<String> keys = GeoHashUtil.getCoverBoundingBoxHashes(minLon, minLat, maxLon, maxLat, BUILDING_GEOHASH_LENGTH);
        Map<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>> buildingCacheAll = buildingCache.getAll(keys);

        return buildingCacheAll.values().stream()
                .flatMap(Optional::stream)
                .flatMap(Set::stream)
                .map(dto -> BuildingBoundSearchResponseDto.of(
                        dto.getId(), dto.getName(), dto.getLongitude(), dto.getLatitude()))
                .toList();
    }

    private void validateMaxGreaterThanMin(Double minLon, Double minLat, Double maxLon, Double maxLat) {
        if (minLat > maxLat) {
            log.error("Invalid latitude bounds: minLat={} > maxLat={}", minLat, maxLat);
            throw new IllegalArgumentException("minLat must be less than or equal to maxLat");
        }
        if (minLon > maxLon) {
            log.error("Invalid longitude bounds: minLon={} > maxLon={}", minLon, maxLon);
            throw new IllegalArgumentException("minLon must be less than or equal to maxLon");
        }
    }

    private void validateBoundsRange(Double minLon, Double minLat, Double maxLon, Double maxLat) {
        if (minLat < -90 || minLat > 90 || maxLat < -90 || maxLat > 90) {
            log.error("Latitude out of bounds: minLat={}, maxLat={}", minLat, maxLat);
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }

        // 경도 범위 검증
        if (minLon < -180 || minLon > 180 || maxLon < -180 || maxLon > 180) {
            log.error("Longitude out of bounds: minLon={}, maxLon={}", minLon, maxLon);
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }
    }

}
