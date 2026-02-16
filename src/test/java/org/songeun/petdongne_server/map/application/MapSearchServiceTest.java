package org.songeun.petdongne_server.map.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.songeun.petdongne_server.address.application.dto.AddressBoundsSearchResponseDto;
import org.songeun.petdongne_server.address.application.service.AddressSearchService;
import org.songeun.petdongne_server.address.domain.RegionAddressLevel;
import org.songeun.petdongne_server.building.application.BuildingBoundSearchResponseDto;
import org.songeun.petdongne_server.building.application.BuildingSearchService;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.map.domain.ZoomLevel;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Set;

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

    private static final Set<String> geoHashes = Set.of("wymsk", "wjklsf");

    @Test
    @DisplayName("지오해시가 같은 클러스터 정보를 반환한다")
    void shouldReturnClusters() {
        // given
        given(zoomLevel.isSupportedInCluster()).willReturn(true);
        given(zoomLevel.toRegionAddressLevel()).willReturn(RegionAddressLevel.SIDO);

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

        given(addressSearchService.searchWithinBounds(geoHashes, RegionAddressLevel.SIDO))
                .willReturn(expectedResponse);

        // when
        List<AddressBoundsSearchResponseDto> result = mapSearchService
                .searchClustersWithinBounds(geoHashes, zoomLevel);

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
                .searchWithinBounds(geoHashes, RegionAddressLevel.SIDO);
        then(zoomLevel)
                .should(times(1))
                .isSupportedInCluster();
    }

    @Test
    @DisplayName("지도 줌 레벨이 클러스터 조회를 지원하지 않으면 예외를 던진다")
    void shouldThrowExceptionWhenZoomLevelIsNotSupported() {
        //given
        given(zoomLevel.isSupportedInCluster()).willReturn(false);

        //when & then
        assertThatThrownBy(() -> mapSearchService.searchClustersWithinBounds(geoHashes, zoomLevel))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("경계 내부 지형물 정보를 반환한다")
    void shouldReturnDetailsWithinBounds() {
        // given
        given(zoomLevel.isSupportedInDetail()).willReturn(true);

        List<BuildingBoundSearchResponseDto> expectedResponse = List.of(
                BuildingBoundSearchResponseDto.builder()
                        .id(1L)
                        .name("서울타워")
                        .longitude(126.9784)
                        .latitude(37.5665)
                        .build(),
                BuildingBoundSearchResponseDto.builder()
                        .id(2L)
                        .name("롯데월드타워")
                        .longitude(127.1028)
                        .latitude(37.5125)
                        .build()
        );

        given(buildingSearchService.searchWithinBounds(anySet())).willReturn(expectedResponse);

        // when
        List<BuildingBoundSearchResponseDto> result = mapSearchService.searchDetailsWithinBounds(geoHashes, zoomLevel);

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
                .searchWithinBounds(anySet());
        then(zoomLevel)
                .should(times(1))
                .isSupportedInDetail();
    }

    @Test
    @DisplayName("지도 줌 레벨이 상세 조회를 지원하지 않으면 예외를 던진다")
    void shouldThrowExceptionWhenZoomLevelIsNotSupportedInDetail() {
        // given
        given(zoomLevel.isSupportedInDetail()).willReturn(false);

        // when & then
        assertThatThrownBy(() -> mapSearchService.searchDetailsWithinBounds(geoHashes, zoomLevel))
                .isInstanceOf(BusinessException.class);

        // verify
        then(zoomLevel)
                .should(times(1))
                .isSupportedInDetail();
    }

}