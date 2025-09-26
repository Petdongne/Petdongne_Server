package org.songeun.petdongne_server.map.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.songeun.petdongne_server.address.application.dto.AddressBoundsSearchResponseDto;
import org.songeun.petdongne_server.address.application.service.AddressSearchService;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

class MapSearchServiceTest extends IntegrationTestSupport {

    @Autowired
    private MapSearchService mapSearchService;

    @MockitoBean
    private AddressSearchService addressSearchService;

    @Test
    @DisplayName("경계 내부 클러스터 정보를 반환한다")
    void shouldReturnClusters() {
        // given
        Double minLon = 126.9784;
        Double minLat = 37.5665;
        Double maxLon = 127.0284;
        Double maxLat = 37.6165;
        Integer level = 11;

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

        given(addressSearchService.searchWithinBounds(minLon, minLat, maxLon, maxLat, level))
                .willReturn(expectedResponse);

        // when
        List<AddressBoundsSearchResponseDto> result = mapSearchService
                .searchClustersWithinBounds(minLon, minLat, maxLon, maxLat, level);

        // then
        assertThat(result).isNotNull();
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
                .searchWithinBounds(minLon, minLat, maxLon, maxLat, level);
    }

}