package org.songeun.petdongne_server.building.application;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.building.fixture.BuildingFixtureFactory;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingBoundSearchQueryResponseDto;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.songeun.petdongne_server.testSupport.PostgresSQLIntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@Transactional
class BuildingSearchServiceTest extends PostgresSQLIntegrationTestSupport {

    @Autowired
    private BuildingSearchService buildingSearchService;

    @Autowired
    private BuildingFixtureFactory fixtureFactory;

    @Test
    @DisplayName("주어진 범위 내의 건물 정보를 조회한다")
    void shouldReturnBuildingsWithinBounds(){
        //given
        Double minLon = 127.0172249;
        Double minLat = 37.4905425;
        Double maxLon = 127.0386825;
        Double maxLat = 37.5024595;

        List<Building> saved = fixtureFactory.makeAndSaveBuildingsWithin(minLon, minLat);

        //when
        List<BuildingBoundSearchQueryResponseDto> result = buildingSearchService
                .searchWithinBounds(minLon, minLat, maxLon, maxLat);

        //then
        assertThat(result).hasSize(saved.size());
        assertThat(result)
                .extracting("longitude", "latitude")
                .containsExactlyInAnyOrderElementsOf(
                        saved.stream()
                                .map(b -> tuple(b.getLongitude(), b.getLatitude()))
                                .toList()
                );
    }

    @Test
    @DisplayName("경계값(최소/최대 위도·경도)을 넣어도 정상 동작한다")
    void shouldReturnBuildingsWithinExtremeBounds(){
        //given
        Double minLon = -180.0000000;
        Double minLat = -90.00000000;
        Double maxLon = 180.0000000;
        Double maxLat = 90.00000000;

        List<Building> saved = fixtureFactory.makeAndSaveBuildingsWithin(minLon, minLat);

        //when
        List<BuildingBoundSearchQueryResponseDto> result = buildingSearchService
                .searchWithinBounds(minLon, minLat, maxLon, maxLat);

        //then
        assertThat(result).hasSize(saved.size());
        assertThat(result)
                .extracting("longitude", "latitude")
                .containsExactlyInAnyOrderElementsOf(
                        saved.stream()
                                .map(b -> tuple(b.getLongitude(), b.getLatitude()))
                                .toList()
                );
    }

    @ParameterizedTest(name = "{index} => minLon:{0}, minLat:{1}, maxLon:{2}, maxLat:{3}")
    @MethodSource("invalidSearchBoundsProvider")
    @DisplayName("유효하지 않은 검색 경계값으로 요청 시 예외를 던진다 (null, 범위 초과, 순서 오류)")
    void shouldThrowExceptionForInvalidBounds(Double minLon, Double minLat, Double maxLon, Double maxLat){
        //when & then
        assertThatThrownBy(() -> buildingSearchService.searchWithinBounds(minLon, minLat, maxLon, maxLat))
                .isInstanceOfAny(IllegalArgumentException.class);
    }

    /**
     * 검증 로직에 위반되는 모든 케이스를 제공하는 정적 팩토리 메서드입니다.
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