package org.songeun.petdongne_server.building.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingBoundSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.repository.BuildingSearchRepository;
import org.songeun.petdongne_server.map.domain.ZoomLevel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BuildingSearchService {

    private final BuildingSearchRepository searchRepository;

    public List<BuildingBoundSearchQueryResponseDto> searchWithinBounds(
            Double minLon, Double minLat, Double maxLon, Double maxLat, ZoomLevel zoomLevel) {
        Assert.notNull(maxLat, "maxLat must not be null");
        Assert.notNull(maxLon, "maxLon must not be null");
        Assert.notNull(minLat, "minLat must not be null");
        Assert.notNull(minLon, "minLon must not be null");

        // 위도 범위 검증
        if (minLat < -90 || minLat > 90 || maxLat < -90 || maxLat > 90) {
            log.error("Latitude out of bounds: minLat={}, maxLat={}", minLat, maxLat);
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }

        // 경도 범위 검증
        if (minLon < -180 || minLon > 180 || maxLon < -180 || maxLon > 180) {
            log.error("Longitude out of bounds: minLon={}, maxLon={}", minLon, maxLon);
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }

        // 논리적 순서 검증
        if (minLat > maxLat) {
            log.error("Invalid latitude bounds: minLat={} > maxLat={}", minLat, maxLat);
            throw new IllegalArgumentException("minLat must be less than or equal to maxLat");
        }
        if (minLon > maxLon) {
            log.error("Invalid longitude bounds: minLon={} > maxLon={}", minLon, maxLon);
            throw new IllegalArgumentException("minLon must be less than or equal to maxLon");
        }

        return searchRepository.findWithinBounds(minLon, minLat, maxLon, maxLat);
    }

}
