package org.songeun.petdongne_server.building.application;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.davidmoten.geo.Coverage;
import com.github.davidmoten.geo.GeoHash;
import org.junit.jupiter.api.*;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.building.domain.BuildingGeoHashLengthProvider;
import org.songeun.petdongne_server.building.fixture.BuildingFixtureFactory;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingGeoHashSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.repository.BuildingSearchRepository;
import org.songeun.petdongne_server.global.util.GeoHashUtil;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.songeun.petdongne_server.testSupport.PostgresSQLIntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BuildingSearchServiceTest extends PostgresSQLIntegrationTestSupport {

    @Autowired
    private BuildingSearchService buildingSearchService;

    @Autowired
    private BuildingFixtureFactory fixtureFactory;

    @MockitoSpyBean
    private BuildingSearchRepository buildingSearchRepository;

    @MockitoSpyBean
    private LoadingCache<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>> buildingCache;

    @MockitoSpyBean
    private CacheLoader<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>> buildingCacheLoader;

    @Autowired
    private BuildingGeoHashLengthProvider buildingGeoHashLengthProvider;

    // fixture ==
    private List<Building> buildingFixture;
    private Set<String> geoHashes;
    private final double minLon = 127.0172249;
    private final double minLat = 37.4905425;
    // == end

    @BeforeAll
    void beforeAll() {
        buildingFixture = fixtureFactory.makeAndSaveBuildingsWithin(minLon, minLat);

        double maxLon = minLon + 0.05;
        double maxLat = minLat + 0.05;
        geoHashes = GeoHashUtil.getCoverBoundingBoxHashes(
                minLon, minLat, maxLon, maxLat, buildingGeoHashLengthProvider.getGeoHashLength());
    }

    @AfterEach
    void tearDown() {
        buildingCache.invalidateAll();
    }

    @Test
    @DisplayName("지오해시로 건물 정보를 조회한다")
    void shouldReturnBuildingsWithinBounds() {
        //given &when
        List<BuildingBoundSearchResponseDto> result = buildingSearchService
                .searchWithinBounds(geoHashes);

        //then
        List<Building> expectedResult = buildingFixture.stream()
                .filter(fixture -> geoHashes.contains(fixture.getGeohash()))
                .toList();

        assertThat(result).hasSize(expectedResult.size());
        assertThat(result)
                .extracting("longitude", "latitude", "geoHash")
                .containsExactlyInAnyOrderElementsOf(
                        expectedResult.stream()
                                .map(b -> tuple(b.getLongitude(), b.getLatitude(), b.getGeohash()))
                                .toList()
                );
    }

    @Test
    @DisplayName("항상 캐시를 통해서 조회해온다.")
    void shouldReturnFetchResultsFromCache() {
        //given
        Map<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>> capturedCacheMap = new HashMap<>();
        doAnswer(invocation -> {
            Map<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>> realResult =
                    (Map<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>>) invocation.callRealMethod();

            capturedCacheMap.putAll(realResult); // 호출 시 값 캡처
            return realResult;
        }).when(buildingCache).getAll(any());

        //when
        List<BuildingBoundSearchResponseDto> result = buildingSearchService.searchWithinBounds(geoHashes);

        //then
        assertSameContent(result, capturedCacheMap);
        assertCacheCalledOnceWithKeys(geoHashes);
    }

    private void assertSameContent(
            List<BuildingBoundSearchResponseDto> result,
            Map<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>> capturedCacheMap) {
        // 캐시 조회 결과 -> 값이 존재하지 않는 캐시 제외
        Set<BuildingGeoHashSearchQueryResponseDto> cached = capturedCacheMap.values().stream()
                .flatMap(Optional::stream)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    private void assertCacheCalledOnceWithKeys(Set<String> keys) {
        verify(buildingCache, times(1)).getAll(keys);
        verify(buildingCache, times(0)).get(any());
    }

    @Test
    @DisplayName("캐시가 존재하면 DB 조회는 발생하지 않는다")
    void shouldStoreFetchedResultsInCacheWithoutReload() throws Exception {
        // given & when
        buildingSearchService.searchWithinBounds(geoHashes);
        buildingSearchService.searchWithinBounds(geoHashes);

        // then
        verify(buildingCacheLoader, times(1)).loadAll(any());
        verify(buildingSearchRepository, times(1)).findByGeoHashes(any(Set.class));
    }

}