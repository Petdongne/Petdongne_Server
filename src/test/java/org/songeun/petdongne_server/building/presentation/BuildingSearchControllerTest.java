package org.songeun.petdongne_server.building.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.building.application.BuildingSearchService;
import org.songeun.petdongne_server.building.application.dto.BuildingDetailResponseDto;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.config.CorsConfig;
import org.songeun.petdongne_server.global.config.ObjectMapperConfig;
import org.songeun.petdongne_server.security.SecurityConfig;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.testSupport.GeometryTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuildingSearchController.class)
@Import({SecurityConfig.class, CorsConfig.class, ObjectMapperConfig.class})
class BuildingSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BuildingSearchService buildingSearchService;

    @DisplayName("건물 상세 정보를 조회한다.")
    @Test
    void getBuildingDetail() throws Exception {
        // given
        Long buildingId = 1L;
        MultiPolygon<G2D> mockPolygon = GeometryTestUtils.multiPolygon();

        BuildingDetailResponseDto responseDto = new BuildingDetailResponseDto(
                "테스트빌딩",
                1,
                100,
                10,
                "2020",
                "01",
                "지번주소",
                "도로명주소",
                "아파트",
                mockPolygon,
                127.0,
                37.0
        );

        given(buildingSearchService.getBuildingDetail(anyLong())).willReturn(responseDto);

        // when // then
        mockMvc.perform(get("/api/v1/buildings/{building_id}", buildingId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data.name").value("테스트빌딩"))
                .andExpect(jsonPath("$.data.dongCount").value(1))
                .andExpect(jsonPath("$.data.householdCount").value(100))
                .andExpect(jsonPath("$.data.topFloorCount").value(10))
                .andExpect(jsonPath("$.data.approvalYear").value("2020"))
                .andExpect(jsonPath("$.data.approvalMonth").value("01"))
                .andExpect(jsonPath("$.data.jibunAddress").value("지번주소"))
                .andExpect(jsonPath("$.data.roadAddress").value("도로명주소"))
                .andExpect(jsonPath("$.data.buildingType").value("아파트"))
                .andExpect(jsonPath("$.data.longitude").value(127.0))
                .andExpect(jsonPath("$.data.latitude").value(37.0));
    }

    @DisplayName("존재하지 않는 건물 ID로 상세 정보를 조회하면 404 에러를 반환한다.")
    @Test
    void getBuildingDetailWithNotFoundId() throws Exception {
        // given
        Long buildingId = 999L;
        given(buildingSearchService.getBuildingDetail(anyLong()))
                .willThrow(new BusinessException(GlobalErrorStatus.NOT_FOUND));

        // when // then
        mockMvc.perform(get("/api/v1/buildings/{building_id}", buildingId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("요청하신 리소스를 찾을 수 없습니다."));
    }
}
