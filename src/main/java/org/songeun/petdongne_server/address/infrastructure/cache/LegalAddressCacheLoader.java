package org.songeun.petdongne_server.address.infrastructure.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.songeun.petdongne_server.address.domain.RegionAddressLevel;
import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressGeoHashSearchQueryResponseDto;
import org.songeun.petdongne_server.address.infrastructure.repository.LegalAddressSearchRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LegalAddressCacheLoader implements CacheLoader<LegalAddressCacheKey, Optional<List<LegalAddressGeoHashSearchQueryResponseDto>>> {

    private final LegalAddressSearchRepository legalAddressSearchRepository;

    @Override
    public Optional<List<LegalAddressGeoHashSearchQueryResponseDto>> load(LegalAddressCacheKey key) throws Exception {
        throw new UnsupportedOperationException("Single-key load is not supported, use loadAll instead.");
    }

    @Override
    public Map<? extends LegalAddressCacheKey, ? extends @NonNull Optional<List<LegalAddressGeoHashSearchQueryResponseDto>>> loadAll(Set<? extends LegalAddressCacheKey> keys) throws Exception {
        // Level은 keys에서 동일하다고 가정
        RegionAddressLevel level = keys.iterator().next().getLevel();

        Set<String> geoHashes = keys.stream()
                .map(LegalAddressCacheKey::getGeoHash)
                .collect(Collectors.toSet());

        List<LegalAddressGeoHashSearchQueryResponseDto> results =
                legalAddressSearchRepository.findByGeoHashAndLevel(geoHashes, level);

        Map<LegalAddressCacheKey, Optional<List<LegalAddressGeoHashSearchQueryResponseDto>>> map = new HashMap<>();
        for (String geoHash : geoHashes) {
            List<LegalAddressGeoHashSearchQueryResponseDto> filtered = results.stream()
                    .filter(r -> r.getGeoHash().equals(geoHash))
                    .collect(Collectors.toList());
            map.put(LegalAddressCacheKey.of(geoHash, level), Optional.of(filtered));
        }

        return map;
    }

}
