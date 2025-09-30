package org.songeun.petdongne_server.building.infrastructure.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingGeoHashSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.repository.BuildingSearchRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BuildingCacheLoader implements CacheLoader<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>> {

    private final BuildingSearchRepository buildingSearchRepository;

    @Override
    public Optional<Set<BuildingGeoHashSearchQueryResponseDto>> load(String key) {
        throw new UnsupportedOperationException("Single-key load is not supported, use loadAll instead.");
    }

    @Override
    public Map<? extends String, ? extends Optional<Set<BuildingGeoHashSearchQueryResponseDto>>> loadAll(
            Set<? extends String> keys) throws Exception {
        Set<BuildingGeoHashSearchQueryResponseDto> withinBounds = buildingSearchRepository.findByGeoHashes((Set<String>) keys);

        Map<String, Set<BuildingGeoHashSearchQueryResponseDto>> grouped = withinBounds.stream()
                .collect(Collectors.groupingBy(
                        BuildingGeoHashSearchQueryResponseDto::getGeohash,
                        Collectors.toSet()
                ));

        return keys.stream()
                .collect(Collectors.toMap(
                        key -> key,
                        key -> Optional.ofNullable(grouped.get(key))
                ));
    }

}
