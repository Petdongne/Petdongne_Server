package org.songeun.petdongne_server.address.presentation.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.address.application.service.AddressOldSearchService;
import org.songeun.petdongne_server.global.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AddressSearchController2.class)
@Import(SecurityConfig.class)
class AddressSearchController2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AddressOldSearchService addressSearchService;

    @Test
    @DisplayName("검색 결과가 없으면 200 OK와 함께 빈 리스트를 반환한다.")
    void shouldReturnEmptyListWhenNoSearchResult() throws Exception {
        //given
        String searchText = "서울특별시 마포구";
        int page = 0;
        int size = 10;

        given(addressSearchService.search(eq(searchText), any()))
                .willReturn(new PageImpl<>(List.of()));

        //when //then
        mockMvc.perform(
                        get("/api/v1/addresses/search")
                                .param("searchText", searchText)
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
                        get("/api/v1/addresses/search")
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("MISSING_REQUEST_PARAMETER"))
                .andExpect(jsonPath("$.message").value("필수 요청 파라미터가 누락되었습니다."))
                .andExpect(jsonPath("$.data.field").value("searchText"))
                .andExpect(jsonPath("$.data.message").value("요청 파라미터 'searchText'는 필수입니다."));
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
                        get("/api/v1/addresses/search")
                                .param("searchText", searchText)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].field").value("searchText"))
                .andExpect(jsonPath("$.data[0].message").value("검색어는 공백일 수 없습니다."));
    }

    @Test
    @DisplayName("검색어에는 한글, 영문, 숫자, 공백만 입력할 수 있다.")
    void shouldRejectWhenSearchTextContainsInvalidCharacters() throws Exception {
        //given
        String searchText = "AND {query: {match: }";
        int page = 0;
        int size = 10;

        //when //then
        mockMvc.perform(
                        get("/api/v1/addresses/search")
                                .param("searchText", searchText)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].field").value("searchText"))
                .andExpect(jsonPath("$.data[0].message").value("검색어는 한글, 영문, 숫자와 공백만 허용됩니다."));
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
                        get("/api/v1/addresses/search")
                                .param("searchText", searchText)
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
                        get("/api/v1/addresses/search")
                                .param("searchText", searchText)
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].field").value("size"))
                .andExpect(jsonPath("$.data[0].message").value("한 페이지당 최대 100개까지만 조회할 수 있습니다."));
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
                        get("/api/v1/addresses/search")
                                .param("searchText", searchText)
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