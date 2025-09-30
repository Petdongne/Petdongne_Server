package org.songeun.petdongne_server.map.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.songeun.petdongne_server.address.application.dto.AddressBoundsSearchResponseDto;
import org.songeun.petdongne_server.building.application.BuildingBoundSearchResponseDto;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingBoundSearchQueryResponseDto;
import org.songeun.petdongne_server.global.config.SecurityConfig;
import org.songeun.petdongne_server.map.application.MapSearchService;
import org.songeun.petdongne_server.map.domain.KakaoZoomLevel;
import org.songeun.petdongne_server.map.domain.KakaoZoomLevelCategory;
import org.songeun.petdongne_server.map.domain.ZoomLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MapSearchController.class)
@Import(SecurityConfig.class)
class MapSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MapSearchService searchService;

    private static final String CLUSTER_BASE_URL = "/api/v1/map/clusters";

    @Nested
    @DisplayName("지도 특정 범위 내 클러스터 정보 조회 (현재: 행정구역 이름 한정)")
    class ClusterEndpointTest{
        @Test
        @DisplayName("유효한 파라미터로 클러스터 조회 요청 시 HTTP 200 OK와 결과를 반환한다")
        void searchClusters_valid () throws Exception {
        // given
        Double minLon = 127.0;
        Double minLat = 37.0;
        Double maxLon = 128.0;
        Double maxLat = 38.0;
        Integer level = 11;

        AddressBoundsSearchResponseDto dto = AddressBoundsSearchResponseDto.builder()
                .name("서울특별시")
                .longitude(127.5)
                .latitude(37.5)
                .regionLevel("시도")
                .build();

        List<AddressBoundsSearchResponseDto> mockResponse = List.of(dto);

        given(searchService.searchClustersWithinBounds(anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(ZoomLevel.class)))
                .willReturn(mockResponse);

        // when & then
        mockMvc.perform(
                        get(CLUSTER_BASE_URL)
                                .param("minLon", String.valueOf(minLon))
                                .param("minLat", String.valueOf(minLat))
                                .param("maxLon", String.valueOf(maxLon))
                                .param("maxLat", String.valueOf(maxLat))
                                .param("level", String.valueOf(level))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("서울특별시"))
                .andExpect(jsonPath("$.data[0].longitude").value(127.5))
                .andExpect(jsonPath("$.data[0].latitude").value(37.5))
                .andExpect(jsonPath("$.data[0].regionLevel").value("시도"));

        verify(searchService).searchClustersWithinBounds(minLon, minLat, maxLon, maxLat, KakaoZoomLevelCategory.from(level));
    }

        @Test
        @DisplayName("조회 결과가 없을 경우 빈 리스트를 반환한다")
        void searchClusters_noResult () throws Exception {
        // given
        Double minLon = 127.0;
        Double minLat = 37.0;
        Double maxLon = 128.0;
        Double maxLat = 38.0;
        Integer level = 7;

        given(searchService.searchClustersWithinBounds(anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(ZoomLevel.class)))
                .willReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(
                        get(CLUSTER_BASE_URL)
                                .param("minLon", String.valueOf(minLon))
                                .param("minLat", String.valueOf(minLat))
                                .param("maxLon", String.valueOf(maxLon))
                                .param("maxLat", String.valueOf(maxLat))
                                .param("level", String.valueOf(level))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());

        verify(searchService).searchClustersWithinBounds(minLon, minLat, maxLon, maxLat, KakaoZoomLevelCategory.from(level));
    }

        @ParameterizedTest(name = "{index} => minLon:{0}, minLat:{1}, maxLon:{2}, maxLat:{3}, level:{4}")
        @CsvSource(value = {
                "180.1, 0.0, 0.0, 0.0, 11, minLon > 180",         // minLon: Max 위반
                "-180.1, 0.0, 0.0, 0.0, 11, minLon < -180",       // minLon: Min 위반
                "0.0, 90.1, 0.0, 0.0, 11, minLat > 90",          // minLat: Max 위반
                "0.0, -90.1, 0.0, 0.0, 11, minLat < -90",        // minLat: Min 위반
                "0.0, 0.0, 180.1, 0.0, 11, maxLon > 180",        // maxLon: Max 위반
                "0.0, 0.0, -180.1, 0.0, 11, maxLon < -180",      // maxLon: Min 위반
                "0.0, 0.0, 0.0, 90.1, 11, maxLat > 90",          // maxLat: Max 위반
                "0.0, 0.0, 0.0, -90.1, 11, maxLat < -90",        // maxLat: Min 위반
        })
        @DisplayName("위도/경도 범위 초과 시 HTTP 400 Bad Request를 반환한다")
        void searchClusters_invalidBounds (
            Double minLon, Double minLat, Double maxLon, Double maxLat, Integer level, String description
    ) throws Exception {
        // when & then
        mockMvc.perform(
                        get(CLUSTER_BASE_URL)
                                .param("minLon", String.valueOf(minLon))
                                .param("minLat", String.valueOf(minLat))
                                .param("maxLon", String.valueOf(maxLon))
                                .param("maxLat", String.valueOf(maxLat))
                                .param("level", String.valueOf(level))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));

        verify(searchService, never()).searchClustersWithinBounds(anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(ZoomLevel.class));
    }

        @ParameterizedTest(name = "{index} => level:{0}")
        @ValueSource(ints = {0, 15, -1, 100})
        @DisplayName("줌 레벨 범위 초과 시 HTTP 400 Bad Request를 반환한다")
        void searchClusters_invalidLevel (Integer level) throws Exception {
        // given
        Double minLon = 127.0;
        Double minLat = 37.0;
        Double maxLon = 128.0;
        Double maxLat = 38.0;

        // when & then
        mockMvc.perform(
                        get(CLUSTER_BASE_URL)
                                .param("minLon", String.valueOf(minLon))
                                .param("minLat", String.valueOf(minLat))
                                .param("maxLon", String.valueOf(maxLon))
                                .param("maxLat", String.valueOf(maxLat))
                                .param("level", String.valueOf(level))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));

        verify(searchService, never()).searchClustersWithinBounds(anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(ZoomLevel.class));
    }

        @ParameterizedTest(name = "{index} => 누락 파라미터: {0}")
        @CsvSource(value = {
                ", 37.0, 128.0, 38.0, 11, minLon",
                "127.0, , 128.0, 38.0, 11, minLat",
                "127.0, 37.0, , 38.0, 11, maxLon",
                "127.0, 37.0, 128.0, , 11, maxLat",
                "127.0, 37.0, 128.0, 38.0, , level"
        })
        @DisplayName("필수 파라미터 누락 시 HTTP 400 Bad Request를 반환한다")
        void searchClusters_missingRequiredParameter (
            String minLon, String minLat, String maxLon, String maxLat, String level, String missingParam
    ) throws Exception {
        // when & then
        MockHttpServletRequestBuilder requestBuilder = get(CLUSTER_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON);

        if (minLon != null) requestBuilder.param("minLon", minLon);
        if (minLat != null) requestBuilder.param("minLat", minLat);
        if (maxLon != null) requestBuilder.param("maxLon", maxLon);
        if (maxLat != null) requestBuilder.param("maxLat", maxLat);
        if (level != null) requestBuilder.param("level", level);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(searchService, never()).searchClustersWithinBounds(anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(ZoomLevel.class));
    }

        @Test
        @DisplayName("유효한 최소/최대값으로 요청 시 성공한다")
        void searchClusters_boundaryValues () throws Exception {
        // given
        given(searchService.searchClustersWithinBounds(anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(ZoomLevel.class)))
                .willReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(
                        get(CLUSTER_BASE_URL)
                                .param("minLon", "-180")
                                .param("minLat", "-90")
                                .param("maxLon", "180")
                                .param("maxLat", "90")
                                .param("level", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        get(CLUSTER_BASE_URL)
                                .param("minLon", "180")
                                .param("minLat", "90")
                                .param("maxLon", "-180")
                                .param("maxLat", "-90")
                                .param("level", "14")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(searchService, times(2)).searchClustersWithinBounds(anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(ZoomLevel.class));
    }
    }


    private static final String DETAIL_BASE_URL = "/api/v1/map/details";

    @Nested
    @DisplayName("지도의 특정 범위 내 존재하는 지형물 조회 (현재: 건물 한정 조회)")
    class DetailEndpointTest {
        @Test
        @DisplayName("유효한 범위로 건물 조회 요청 시 HTTP 200 OK와 결과를 반환한다")
        void searchBuildingsWithinBounds_valid () throws Exception {
        // given
        Double minLon = 127.0;
        Double minLat = 37.0;
        Double maxLon = 128.0;
        Double maxLat = 38.0;
        Integer level = 1;

        BuildingBoundSearchResponseDto dto = BuildingBoundSearchResponseDto.builder()
                .id(1L)
                .name("테스트 빌딩")
                .longitude(127.5)
                .latitude(37.5)
                .build();

        List<BuildingBoundSearchResponseDto> mockResponse = List.of(dto);

        given(searchService.searchDetailsWithinBounds(anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(ZoomLevel.class)))
                .willReturn(mockResponse);

        // when & then
        mockMvc.perform(
                        get(DETAIL_BASE_URL)
                                .param("minLon", String.valueOf(minLon))
                                .param("minLat", String.valueOf(minLat))
                                .param("maxLon", String.valueOf(maxLon))
                                .param("maxLat", String.valueOf(maxLat))
                                .param("level", String.valueOf(level))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("테스트 빌딩"))
                .andExpect(jsonPath("$.data[0].longitude").value(127.5))
                .andExpect(jsonPath("$.data[0].latitude").value(37.5));

        verify(searchService).searchDetailsWithinBounds(minLon, minLat, maxLon, maxLat, KakaoZoomLevel.from(level));
        }

        @Test
        @DisplayName("조회 결과가 없을 경우 빈 리스트를 반환한다")
        void searchBuildingsWithinBounds_noResult () throws Exception {
        // given
        Double minLon = 127.0;
        Double minLat = 37.0;
        Double maxLon = 128.0;
        Double maxLat = 38.0;
        Integer level = 1;

        given(searchService.searchDetailsWithinBounds(anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(ZoomLevel.class)))
                .willReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(
                        get(DETAIL_BASE_URL)
                                .param("minLat", String.valueOf(minLat))
                                .param("minLon", String.valueOf(minLon))
                                .param("maxLat", String.valueOf(maxLat))
                                .param("maxLon", String.valueOf(maxLon))
                                .param("level", String.valueOf(level))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
        }

        @ParameterizedTest(name = "{index} => minLat:{0}, minLon:{1}, maxLat:{2}, maxLon:{3}")
        @CsvSource(value = {
                "90.1, 0.0, 0.0, 0.0, minLat > 90",         // minLat: Max 위반
                "-90.1, 0.0, 0.0, 0.0, minLat < -90",        // minLat: Min 위반
                "0.0, 180.1, 0.0, 0.0, minLon > 180",         // minLon: Max 위반
                "0.0, -180.1, 0.0, 0.0, minLon < -180",       // minLon: Min 위반
                "0.0, 0.0, 90.1, 0.0, maxLat > 90",         // maxLat: Max 위반
                "0.0, 0.0, -90.1, 0.0, maxLat < -90",        // maxLat: Min 위반
                "0.0, 0.0, 0.0, 180.1, maxLon > 180",         // maxLon: Max 위반
                "0.0, 0.0, 0.0, -180.1, maxLon < -180",       // maxLon: Min 위반
        })
        @DisplayName("위도/경도 범위 초과 시 HTTP 400 Bad Request를 반환한다")
        void searchBuildingsWithinBounds_invalidBounds (
            Double minLat, Double minLon, Double maxLat, Double maxLon, String description) throws Exception {
        // when & then
        mockMvc.perform(
                        get(DETAIL_BASE_URL)
                                .param("minLat", String.valueOf(minLat))
                                .param("minLon", String.valueOf(minLon))
                                .param("maxLat", String.valueOf(maxLat))
                                .param("maxLon", String.valueOf(maxLon))
                                .param("level", "11")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));

        verify(searchService, never()).searchDetailsWithinBounds(anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(ZoomLevel.class));
        }
    }

}