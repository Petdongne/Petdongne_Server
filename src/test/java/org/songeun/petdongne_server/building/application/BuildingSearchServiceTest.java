package org.songeun.petdongne_server.building.application;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.davidmoten.geo.Coverage;
import com.github.davidmoten.geo.GeoHash;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.building.fixture.BuildingFixtureFactory;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingGeoHashSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.repository.BuildingSearchRepository;
import org.songeun.petdongne_server.global.util.GeoHashUtil;
import org.songeun.petdongne_server.map.domain.KakaoZoomLevel;
import org.songeun.petdongne_server.map.domain.ZoomLevel;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.songeun.petdongne_server.testSupport.PostgresSQLIntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    // fixture ==
    private List<Building> expectedResult;
    private final Double minLon = 127.0172249;
    private final Double minLat = 37.4905425;
    private final ZoomLevel zoomLevel = KakaoZoomLevel.from(3);
    int geohashLength= BuildingSearchService.BUILDING_GEOHASH_LENGTH; // todo 수정 필요
    // == end

    @BeforeAll
    void beforeAll() {
        expectedResult = fixtureFactory.makeAndSaveBuildingsWithin(minLon, minLat);
    }

    @AfterEach
    void tearDown() {
        buildingCache.invalidateAll();
    }

    @Test
    @DisplayName("주어진 범위 내의 건물 정보를 조회한다")
    void shouldReturnBuildingsWithinBounds() {
        //given
        Double maxLon = minLon + 0.01;
        Double maxLat = minLat + 0.01;

        //when
        List<BuildingBoundSearchResponseDto> result = buildingSearchService
                .searchWithinBounds(minLon, minLat, maxLon, maxLat, zoomLevel);

        //then
        assertThat(result).hasSize(expectedResult.size());
        assertThat(result)
                .extracting("longitude", "latitude")
                .containsExactlyInAnyOrderElementsOf(
                        expectedResult.stream()
                                .map(b -> tuple(b.getLongitude(), b.getLatitude()))
                                .toList()
                );
    }

/*    @Test
    @DisplayName("경계값(최소/최대 위도·경도)을 넘지 않으면 정상 동작한다")
    void shouldReturnBuildingsWithinExtremeBounds() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //given
        Double minLon = -180.0000000;
        Double minLat = -90.00000000;
        Double maxLon = 180.0000000;
        Double maxLat = 90.00000000;

        List<Building> expectedResult = fixtureFactory.makeAndSaveBuildingsWithin(minLon, minLat);

        //when
        List<BuildingBoundSearchResponseDto> result = buildingSearchService
                .searchWithinBounds(minLon, minLat, maxLon, maxLat, zoomLevel);

        //then
        assertThat(result).hasSize(expectedResult.size());
        assertThat(result)
                .extracting("longitude", "latitude")
                .containsExactlyInAnyOrderElementsOf(
                        expectedResult.stream()
                                .map(b -> tuple(b.getLongitude(), b.getLatitude()))
                                .toList()
                );
    }*/

    @Test
    @DisplayName("항상 캐시를 통해서 조회해온다.")
    void shouldReturnFetchResultsFromCache() {
        //given
        Double maxLon = minLon + 0.01;
        Double maxLat = minLat + 0.01;

        Map<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>> capturedCacheMap = new HashMap<>();
        doAnswer(invocation -> {
            Set<String> keys = invocation.getArgument(0);
            Map<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>> realResult =
                    (Map<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>>) invocation.callRealMethod();

            capturedCacheMap.putAll(realResult); // 호출 시 값 캡처
            return realResult;
        }).when(buildingCache).getAll(any());

        //when
        List<BuildingBoundSearchResponseDto> result = buildingSearchService.searchWithinBounds(
                minLon, minLat, maxLon, maxLat, zoomLevel);

        //then
        Set<String> keys = GeoHashUtil.getCoverBoundingBoxHashes(minLon, minLat, maxLon, maxLat, geohashLength);
        assertSameContent(result, expectedResult, capturedCacheMap);
        assertCacheCalledOnceWithKeys(keys);
    }

    private void assertSameContent(
            List<BuildingBoundSearchResponseDto> result,
            List<Building> expectedResult,
            Map<String, Optional<Set<BuildingGeoHashSearchQueryResponseDto>>> capturedCacheMap) {
        // 캐시 조회 결과 -> 값이 존재하지 않는 캐시 제외
        Set<BuildingGeoHashSearchQueryResponseDto> cached = capturedCacheMap.values().stream()
                .flatMap(Optional::stream)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        var extracting = assertThat(result).extracting("id", "latitude", "longitude");
        extracting.containsExactlyInAnyOrderElementsOf(expectedResult.stream()
                .map(building ->
                        tuple(building.getId(), building.getLatitude(), building.getLongitude()))
                .toList()
        );
        extracting.containsExactlyInAnyOrderElementsOf(cached.stream()
                .map(c -> tuple(c.getId(), c.getLatitude(), c.getLongitude())).toList()
        );
    }

    private void assertCacheCalledOnceWithKeys(Set<String> keys) {
        verify(buildingCache, times(1)).getAll(keys);
    }

    @Test
    @DisplayName("캐시가 존재하면 DB 조회는 발생하지 않는다")
    void shouldStoreFetchedResultsInCacheWithoutReload() throws Exception {
        // given
        Double maxLon = minLon + 0.01;
        Double maxLat = minLat + 0.01;

        Coverage coverBoundingBox = GeoHash.coverBoundingBox(
                maxLat, minLon, minLat, maxLon, geohashLength
        );
        Set<String> hashes = coverBoundingBox.getHashes();

        // when
        buildingSearchService.searchWithinBounds(minLon, minLat, maxLon, maxLat, zoomLevel);
        buildingSearchService.searchWithinBounds(minLon, minLat, maxLon, maxLat, zoomLevel);

        // then
        verify(buildingCacheLoader, times(1)).loadAll(any());
        verify(buildingSearchRepository, times(1)).findByGeoHashes(any(Set.class));
    }

    private void assertCacheLoaderCalledOnce(Set<String> hashes) throws Exception {
        verify(buildingCacheLoader, times(1)).loadAll(any());
    }

    @ParameterizedTest(name = "{index} => minLon:{0}, minLat:{1}, maxLon:{2}, maxLat:{3}")
    @MethodSource("invalidSearchBoundsProvider")
    @DisplayName("유효하지 않은 검색 경계값으로 요청 시 예외를 던진다 (null, 범위 초과, 순서 오류)")
    void shouldThrowExceptionForInvalidBounds(Double minLon, Double minLat, Double maxLon, Double maxLat) {
        //when & then
        assertThatThrownBy(() -> buildingSearchService.searchWithinBounds(minLon, minLat, maxLon, maxLat, KakaoZoomLevel.from(2)))
                .isInstanceOfAny(IllegalArgumentException.class);
    }

    /**
     * 검증 로직에 위반되는 모든 케이스를 제공하는 정적 팩토리 메서드입니다.
     *
     * @return 테스트 인자 스트림
     */
    static Stream<Arguments> invalidSearchBoundsProvider() {
        final Double VALID_MIN_LON = 127.0;
        final Double VALID_MIN_LAT = 37.0;
        final Double VALID_MAX_LON = 128.0;
        final Double VALID_MAX_LAT = 38.0;

        return Stream.of(
                // 1. null 검증 케이스
                Arguments.of(null, VALID_MIN_LAT, VALID_MAX_LON, VALID_MAX_LAT), // minLon == null
                Arguments.of(VALID_MIN_LON, null, VALID_MAX_LON, VALID_MAX_LAT), // minLat == null
                Arguments.of(VALID_MIN_LON, VALID_MIN_LAT, null, VALID_MAX_LAT), // maxLon == null
                Arguments.of(VALID_MIN_LON, VALID_MIN_LAT, VALID_MAX_LON, null), // maxLat == null

                // 2. 범위 초과 케이스
                Arguments.of(-180.1, VALID_MIN_LAT, VALID_MAX_LON, VALID_MAX_LAT), // minLon 범위 미달 (<-180)
                Arguments.of(VALID_MIN_LON, 90.1, VALID_MAX_LON, VALID_MAX_LAT),  // minLat 범위 초과 (>90)
                Arguments.of(180.1, VALID_MIN_LAT, VALID_MAX_LON, VALID_MAX_LAT), // maxLon 범위 초과 (>180)
                Arguments.of(VALID_MIN_LON, -90.1, VALID_MAX_LON, VALID_MAX_LAT),  // maxLat 범위 미달 (<-90)

                // 3. 순서 오류 케이스
                Arguments.of(VALID_MAX_LON, VALID_MIN_LAT, VALID_MIN_LON, VALID_MAX_LAT), // minLon > maxLon
                Arguments.of(VALID_MIN_LON, VALID_MAX_LAT, VALID_MAX_LON, VALID_MIN_LAT)  // minLat > maxLat
        );
    }

}