package org.songeun.petdongne_server.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.cache.LegalAddressCacheKey;
import org.songeun.petdongne_server.address.infrastructure.cache.LegalAddressCacheLoader;
import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressGeoHashSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingGeoHashSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.cache.BuildingCacheLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {

    private final BuildingCacheLoader buildingCacheLoader;
    private final LegalAddressCacheLoader legalAddressCacheLoader;

    @Bean
    public LoadingCache<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>> buildingCache() {
        return Caffeine.newBuilder()
                .maximumSize(1_000_000)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(buildingCacheLoader);
    }

    @Bean
    public LoadingCache<LegalAddressCacheKey, Optional<List<LegalAddressGeoHashSearchQueryResponseDto>>> legalAddressCache() {
        return Caffeine.newBuilder()
                .maximumSize(30_000)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(legalAddressCacheLoader);
    }

}

