package org.songeun.petdongne_server.map.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.songeun.petdongne_server.address.application.dto.AddressBoundsSearchResponseDto;
import org.songeun.petdongne_server.address.application.service.AddressSearchService;
import org.songeun.petdongne_server.building.application.BuildingSearchService;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingBoundSearchQueryResponseDto;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.map.domain.ZoomLevel;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

class MapSearchServiceTest extends IntegrationTestSupport {

    @Autowired
    private MapSearchService mapSearchService;

    @MockitoBean
    private AddressSearchService addressSearchService;

    @MockitoBean
    private BuildingSearchService buildingSearchService;

    @Mock
    private ZoomLevel zoomLevel;

    @Test
    @DisplayName("경계 내부 클러스터 정보를 반환한다")
    void shouldReturnClusters() {
        // given
        Double minLon = 126.9784;
        Double minLat = 37.5665;
        Double maxLon = 127.0284;
        Double maxLat = 37.6165;
        given(zoomLevel.isSupportedInCluster()).willReturn(true);

        List<AddressBoundsSearchResponseDto> expectedResponse = List.of(
                AddressBoundsSearchResponseDto.builder()
                        .name("서울특별시")
                        .longitude(126.9780)
                        .latitude(37.5665)
                        .regionLevel("시도")
                        .build(),
                AddressBoundsSearchResponseDto.builder()
                        .name("경기도")
                        .longitude(127.0084)
                        .latitude(37.5965)
                        .regionLevel("시도")
                        .build()
        );

        given(addressSearchService.searchWithinBounds(minLon, minLat, maxLon, maxLat, zoomLevel))
                .willReturn(expectedResponse);

        // when
        List<AddressBoundsSearchResponseDto> result = mapSearchService
                .searchClustersWithinBounds(minLon, minLat, maxLon, maxLat, zoomLevel);

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting("name", "longitude", "latitude", "regionLevel")
                .containsExactlyInAnyOrder(
                        tuple("서울특별시", 126.9780, 37.5665, "시도"),
                        tuple("경기도", 127.0084, 37.5965, "시도")
                );

        // verify
        then(addressSearchService)
                .should(times(1))
                .searchWithinBounds(minLon, minLat, maxLon, maxLat, zoomLevel);
        then(zoomLevel)
                .should(times(1))
                .isSupportedInCluster();
    }

    @Test
    @DisplayName("지도 줌 레벨이 클러스터 조회를 지원하지 않으면 예외를 던진다")
    void shouldThrowExceptionWhenZoomLevelIsNotSupported() {
        //given
        Double minLon = 126.9784;
        Double minLat = 37.5665;
        Double maxLon = 127.0284;
        Double maxLat = 37.6165;
        given(zoomLevel.isSupportedInCluster()).willReturn(false);

        //when & then
        assertThatThrownBy(() -> mapSearchService.searchClustersWithinBounds(minLon, minLat, maxLon, maxLat, zoomLevel))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("경계 내부 건물 상세 정보를 반환한다")
    void shouldReturnDetailsWithinBounds() {
        // given
        Double minLon = 126.9784;
        Double minLat = 37.5665;
        Double maxLon = 127.0284;
        Double maxLat = 37.6165;
        given(zoomLevel.isSupportedInDetail()).willReturn(true);

        List<BuildingBoundSearchQueryResponseDto> expectedResponse = List.of(
                BuildingBoundSearchQueryResponseDto.builder()
                        .id(1L)
                        .name("서울타워")
                        .longitude(126.9784)
                        .latitude(37.5665)
                        .build(),
                BuildingBoundSearchQueryResponseDto.builder()
                        .id(2L)
                        .name("롯데월드타워")
                        .longitude(127.1028)
                        .latitude(37.5125)
                        .build()
        );

        given(buildingSearchService.searchWithinBounds(minLon, minLat, maxLon, maxLat, zoomLevel))
                .willReturn(expectedResponse);

        // when
        List<BuildingBoundSearchQueryResponseDto> result = mapSearchService
                .searchDetailsWithinBounds(minLon, minLat, maxLon, maxLat, zoomLevel);

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting("id", "name", "longitude", "latitude")
                .containsExactlyInAnyOrder(
                        tuple(1L, "서울타워", 126.9784, 37.5665),
                        tuple(2L, "롯데월드타워", 127.1028, 37.5125)
                );

        // verify
        then(buildingSearchService)
                .should(times(1))
                .searchWithinBounds(minLon, minLat, maxLon, maxLat, zoomLevel);
        then(zoomLevel)
                .should(times(1))
                .isSupportedInDetail();
    }

    @Test
    @DisplayName("지도 줌 레벨이 상세 조회를 지원하지 않으면 예외를 던진다")
    void shouldThrowExceptionWhenZoomLevelIsNotSupportedInDetail() {
        // given
        Double minLon = 126.9784;
        Double minLat = 37.5665;
        Double maxLon = 127.0284;
        Double maxLat = 37.6165;
        given(zoomLevel.isSupportedInDetail()).willReturn(false);

        // when & then
        assertThatThrownBy(() -> mapSearchService.searchDetailsWithinBounds(minLon, minLat, maxLon, maxLat, zoomLevel))
                .isInstanceOf(BusinessException.class);

        // verify
        then(zoomLevel)
                .should(times(1))
                .isSupportedInDetail();
    }

}