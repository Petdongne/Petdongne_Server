package org.songeun.petdongne_server.building.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.songeun.petdongne_server.address.presentation.AddressSearchController;
import org.songeun.petdongne_server.building.application.BuildingSearchService;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingBoundSearchQueryResponseDto;
import org.songeun.petdongne_server.global.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BuildingSearchController.class)
@Import(SecurityConfig.class)
class BuildingSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BuildingSearchService buildingSearchService;

    private static final String BASE_URL = "/api/v1/buildings";

    @Test
    @DisplayName("유효한 범위로 건물 조회 요청 시 HTTP 200 OK와 결과를 반환한다")
    void searchBuildingsWithinBounds_valid() throws Exception {
        // given
        Double minLon = 127.0;
        Double minLat = 37.0;
        Double maxLon = 128.0;
        Double maxLat = 38.0;

        BuildingBoundSearchQueryResponseDto dto = BuildingBoundSearchQueryResponseDto.builder()
                .id(1L)
                .name("테스트 빌딩")
                .longitude(127.5)
                .latitude(37.5)
                .build();

        List<BuildingBoundSearchQueryResponseDto> mockResponse = List.of(dto);

        given(buildingSearchService.searchWithinBounds(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .willReturn(mockResponse);

        // when & then
        mockMvc.perform(
                        get(BASE_URL)
                                .param("minLon", String.valueOf(minLon))
                                .param("minLat", String.valueOf(minLat))
                                .param("maxLon", String.valueOf(maxLon))
                                .param("maxLat", String.valueOf(maxLat))
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

        verify(buildingSearchService).searchWithinBounds(minLon, minLat, maxLon, maxLat);
    }

    @Test
    @DisplayName("조회 결과가 없을 경우 빈 리스트를 반환한다")
    void searchBuildingsWithinBounds_noResult() throws Exception {
        // given
        Double minLon = 127.0;
        Double minLat = 37.0;
        Double maxLon = 128.0;
        Double maxLat = 38.0;

        given(buildingSearchService.searchWithinBounds(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .willReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(
                        get(BASE_URL)
                                .param("minLat", String.valueOf(minLat))
                                .param("minLon", String.valueOf(minLon))
                                .param("maxLat", String.valueOf(maxLat))
                                .param("maxLon", String.valueOf(maxLon))
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
    void searchBuildingsWithinBounds_invalidBounds(
            Double minLat, Double minLon, Double maxLat, Double maxLon, String description
    ) throws Exception {
        // when & then
        mockMvc.perform(
                        get(BASE_URL)
                                .param("minLat", String.valueOf(minLat))
                                .param("minLon", String.valueOf(minLon))
                                .param("maxLat", String.valueOf(maxLat))
                                .param("maxLon", String.valueOf(maxLon))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));

        verify(buildingSearchService, never()).searchWithinBounds(anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }

}