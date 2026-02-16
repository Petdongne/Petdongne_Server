package org.songeun.petdongne_server.map.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.songeun.petdongne_server.address.application.dto.AddressBoundsSearchResponseDto;
import org.songeun.petdongne_server.building.application.BuildingBoundSearchResponseDto;
import org.songeun.petdongne_server.security.CorsConfig;
import org.songeun.petdongne_server.security.SecurityConfig;
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
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MapSearchController.class)
@Import({SecurityConfig.class, CorsConfig.class})
class MapSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MapSearchService searchService;

    private static final String CLUSTER_BASE_URL = "/api/v1/map/clusters";

    private static final Set<String> MOCK_GEOHASHES = Set.of("u4pruydqq", "u4pruydqr");

    @Nested
    @DisplayName("지도 특정 범위 내 클러스터 정보 조회 (GeoHash 기반)")
    class ClusterEndpointTest{

        @Test
        @DisplayName("유효한 파라미터로 클러스터 조회 요청 시 HTTP 200 OK와 결과를 반환한다")
        void searchClusters_valid () throws Exception {
            // given
            Integer level = 11;

            AddressBoundsSearchResponseDto dto = AddressBoundsSearchResponseDto.builder()
                    .name("서울특별시")
                    .longitude(127.5)
                    .latitude(37.5)
                    .regionLevel("시도")
                    .build();

            List<AddressBoundsSearchResponseDto> mockResponse = List.of(dto);

            given(searchService.searchClustersWithinBounds(anySet(), any(ZoomLevel.class)))
                    .willReturn(mockResponse);

            // when & then
            mockMvc.perform(
                            get(CLUSTER_BASE_URL)
                                    .param("geoHashes", MOCK_GEOHASHES.toArray(new String[0]))
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

            verify(searchService).searchClustersWithinBounds(eq(MOCK_GEOHASHES), eq(KakaoZoomLevelCategory.from(level)));
        }

        @Test
        @DisplayName("조회 결과가 없을 경우 빈 리스트를 반환한다")
        void searchClusters_noResult () throws Exception {
            // given
            Integer level = 7;

            given(searchService.searchClustersWithinBounds(anySet(), any(ZoomLevel.class)))
                    .willReturn(Collections.emptyList());

            // when & then
            mockMvc.perform(
                            get(CLUSTER_BASE_URL)
                                    .param("geoHashes", MOCK_GEOHASHES.toArray(new String[0]))
                                    .param("level", String.valueOf(level))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isEmpty());

            verify(searchService).searchClustersWithinBounds(eq(MOCK_GEOHASHES), eq(KakaoZoomLevelCategory.from(level)));
        }

        @ParameterizedTest(name = "{index} => level:{0}")
        @ValueSource(ints = {0, 15, -1, 100})
        @DisplayName("줌 레벨 범위 초과 시 HTTP 400 Bad Request를 반환한다")
        void searchClusters_invalidLevel (Integer level) throws Exception {
            // when & then
            mockMvc.perform(
                            get(CLUSTER_BASE_URL)
                                    .param("geoHashes", MOCK_GEOHASHES.toArray(new String[0]))
                                    .param("level", String.valueOf(level))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));

            verify(searchService, never()).searchClustersWithinBounds(anySet(), any(ZoomLevel.class));
        }

        @ParameterizedTest(name = "{index} => 누락 파라미터: {2}")
        @CsvSource(value = {
                ", 11, geoHashes",
                "u4pruydqq, , level"
        })
        @DisplayName("필수 파라미터 누락 시 HTTP 400 Bad Request를 반환한다")
        void searchClusters_missingRequiredParameter (
                String geohash, String level, String missingParam
        ) throws Exception {
            // when & then
            MockHttpServletRequestBuilder requestBuilder = get(CLUSTER_BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON);

            if (geohash != null) requestBuilder.param("geoHashes", geohash);
            if (level != null) requestBuilder.param("level", level);

            mockMvc.perform(requestBuilder)
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(searchService, never()).searchClustersWithinBounds(anySet(), any(ZoomLevel.class));
        }
    }


    private static final String DETAIL_BASE_URL = "/api/v1/map/details";

    @Nested
    @DisplayName("지도의 특정 범위 내 존재하는 지형물 조회 (현재: 건물 한정 조회)")
    class DetailEndpointTest {
        @Test
        @DisplayName("유효한 GeoHashes와 레벨로 건물 조회 요청 시 HTTP 200 OK와 결과를 반환한다")
        void searchBuildingsWithinBounds_valid () throws Exception {
            // given
            Integer level = 1;

            BuildingBoundSearchResponseDto dto = BuildingBoundSearchResponseDto.builder()
                    .id(1L)
                    .name("테스트 빌딩")
                    .longitude(127.5)
                    .latitude(37.5)
                    .build();

            List<BuildingBoundSearchResponseDto> mockResponse = List.of(dto);

            // 변경: GeoHashes와 ZoomLevel을 인자로 받도록 수정
            given(searchService.searchDetailsWithinBounds(anySet(), any(KakaoZoomLevel.class)))
                    .willReturn(mockResponse);

            // when & then
            mockMvc.perform(
                            get(DETAIL_BASE_URL)
                                    .param("geoHashes", MOCK_GEOHASHES.toArray(new String[0]))
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

            verify(searchService).searchDetailsWithinBounds(eq(MOCK_GEOHASHES), eq(KakaoZoomLevel.from(level)));
        }

        @Test
        @DisplayName("조회 결과가 없을 경우 빈 리스트를 반환한다")
        void searchBuildingsWithinBounds_noResult () throws Exception {
            // given
            Integer level = 1;

            given(searchService.searchDetailsWithinBounds(anySet(), any(KakaoZoomLevel.class)))
                    .willReturn(Collections.emptyList());

            // when & then
            mockMvc.perform(
                            get(DETAIL_BASE_URL)
                                    // 변경: bounds 대신 geoHashes 파라미터 사용
                                    .param("geoHashes", MOCK_GEOHASHES.toArray(new String[0]))
                                    .param("level", String.valueOf(level))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isEmpty());

            verify(searchService).searchDetailsWithinBounds(eq(MOCK_GEOHASHES), eq(KakaoZoomLevel.from(level)));
        }

        @ParameterizedTest(name = "{index} => level:{0}")
        @ValueSource(ints = {0, 15, -1, 100})
        @DisplayName("줌 레벨 범위 초과 시 HTTP 400 Bad Request를 반환한다")
        void searchBuildingsWithinBounds_invalidLevel (Integer level) throws Exception {
            // when & then
            mockMvc.perform(
                            get(DETAIL_BASE_URL)
                                    .param("geoHashes", MOCK_GEOHASHES.toArray(new String[0]))
                                    .param("level", String.valueOf(level))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));

            verify(searchService, never()).searchDetailsWithinBounds(anySet(), any(KakaoZoomLevel.class));
        }

        @Test
        @DisplayName("geoHashes 누락 시 HTTP 400 Bad Request를 반환한다")
        void searchBuildingsWithinBounds_missingGeoHashes () throws Exception {
            // when & then
            mockMvc.perform(
                            get(DETAIL_BASE_URL)
                                    .param("level", "11")
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(searchService, never()).searchDetailsWithinBounds(anySet(), any(KakaoZoomLevel.class));
        }

        @Test
        @DisplayName("level 누락 시 HTTP 400 Bad Request를 반환한다")
        void searchBuildingsWithinBounds_missingLevel () throws Exception {
            // when & then
            mockMvc.perform(
                            get(DETAIL_BASE_URL)
                                    .param("geoHashes", MOCK_GEOHASHES.toArray(new String[0]))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(searchService, never()).searchDetailsWithinBounds(anySet(), any(KakaoZoomLevel.class));
        }
    }

}