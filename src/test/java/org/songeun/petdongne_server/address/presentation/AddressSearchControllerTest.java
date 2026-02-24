package org.songeun.petdongne_server.address.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.address.application.dto.AddressSearchRequestDto;
import org.songeun.petdongne_server.address.application.service.AddressSearchService;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AddressSearchControllerTest extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AddressSearchService addressSearchService;

    public static final String REGION_SEARCH_API_URI = "/api/v1/addresses/search";

    @Test
    @DisplayName("검색 결과가 없으면 200 OK와 함께 빈 리스트를 반환한다.")
    void shouldReturnEmptyListWhenNoSearchResult() throws Exception {
        //given
        String searchText = "서울특별시 마포구";
        int page = 0;
        int size = 10;
        AddressSearchRequestDto requestDto = AddressSearchRequestDto.of(searchText, page, size);

        given(addressSearchService.searchByText(requestDto))
                .willReturn(new SliceImpl<>(List.of()) {
        });

        //when //then
        mockMvc.perform(
                        get(REGION_SEARCH_API_URI)
                                .param("query", searchText)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data.currentPage").value(0))
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.items.length()").value(0));
    }

    @Test
    @DisplayName("검색어는 필수 입력값이다.")
    void shouldRejectWhenSearchTextIsMissing() throws Exception {
        //given
        int page = 0;
        int size = 10;

        //when //then
        mockMvc.perform(
                        get(REGION_SEARCH_API_URI)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("MISSING_REQUEST_PARAMETER"))
                .andExpect(jsonPath("$.message").value("필수 요청 파라미터가 누락되었습니다."))
                .andExpect(jsonPath("$.data.field").value("query"))
                .andExpect(jsonPath("$.data.message").value("Request param or part 'query'는 필수입니다."));
    }

    @Test
    @DisplayName("검색어는 공백일 수 없다.")
    void shouldRejectWhenSearchTextIsBlank() throws Exception {
        //given
        String searchText = "  ";
        int page = 0;
        int size = 10;

        //when //then
        mockMvc.perform(
                        get(REGION_SEARCH_API_URI)
                                .param("query", searchText)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].field").value("query"))
                .andExpect(jsonPath("$.data[0].message").value("검색어는 공백일 수 없습니다."));
    }

    @Test
    @DisplayName("페이지 번호는 0 이상이어야 한다.")
    void shouldRejectWhenPageIsNegative() throws Exception {
        //given
        String searchText = "고양이 동네";
        int page = -1;
        int size = 10;

        //when //then
        mockMvc.perform(
                        get(REGION_SEARCH_API_URI)
                                .param("query", searchText)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].field").value("page"))
                .andExpect(jsonPath("$.data[0].message").value("페이지 번호는 0 이상이어야 합니다."));
    }

    @Test
    @DisplayName("조회 개수는 100 이하여야 한다.")
    void shouldRejectWhenSizeExceedsMaxLimit() throws Exception {
        //given
        String searchText = "고양이 동네";
        int page = 1;
        int size = 101;

        //when //then
        mockMvc.perform(
                        get(REGION_SEARCH_API_URI)
                                .param("query", searchText)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].field").value("size"))
                .andExpect(jsonPath("$.data[0].message").value("최대 요청 가능 수는 100입니다."));
    }

    @Test
    @DisplayName("조회 개수는 1 이상이어야 한다.")
    void shouldRejectWhenSizeIsZeroOrNegative() throws Exception {
        //given
        String searchText = "고양이 동네";
        int page = 1;
        int size = 0;

        //when //then
        mockMvc.perform(
                        get(REGION_SEARCH_API_URI)
                                .param("query", searchText)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].field").value("size"))
                .andExpect(jsonPath("$.data[0].message").value("size는 1 이상이어야 합니다."));
    }

}