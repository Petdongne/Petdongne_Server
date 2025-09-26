package org.songeun.petdongne_server.address.application;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.songeun.petdongne_server.address.domain.LegalAddress;
import org.songeun.petdongne_server.address.fixture.LegalAddressFixture;
import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressSearchQueryResponseDto;
import org.songeun.petdongne_server.address.infrastructure.repository.LegalAddressCoreRepository;
import org.songeun.petdongne_server.address.infrastructure.repository.LegalAddressSearchRepository;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.global.search.OrderedTokens;
import org.songeun.petdongne_server.testSupport.PostgresSQLIntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddressSearchServiceTest extends PostgresSQLIntegrationTestSupport {

    @Autowired
    private AddressSearchService addressSearchService;

    @MockitoSpyBean
    private LegalAddressSearchRepository searchRepository;

    @Autowired
    private LegalAddressCoreRepository coreRepository;

    private final List<LegalAddress> fixture = LegalAddressFixture.createIncheonAddress(37.1326117, 125.2422193);

    @BeforeAll
    void beforeAll() {
        coreRepository.saveAll(fixture);
    }

    @AfterAll
    void afterAll() {
        coreRepository.deleteAll(fixture);
    }

    @Test
    @DisplayName("법정동 주소를 검색한다.")
    void shouldSearchLegalAddresses(){
        //given
        String searchText = "인천광역시 부평구";
        AddressSearchRequestDto request = AddressSearchRequestDto.of(searchText, 0, 10);

        //when
        Slice<LegalAddressSearchQueryResponseDto> result = addressSearchService.searchByText(request);

        //then
        assertThat(result.getNumberOfElements()).isEqualTo(3);
        assertThat(result.getContent()).extracting(LegalAddressSearchQueryResponseDto::getFullAddress)
                .containsExactly(
                        "인천광역시 부평구 부평동",
                        "인천광역시 부평구 삼산동",
                        "인천광역시 부평구 청천동"
                );
    }

    @Test
    @DisplayName("주어진 검색어가 한 글자일 때, 해당 글자로 시작하는 주소를 반환한다.")
    void shouldReturnAddressesStartingWithSingleCharacter(){
        //given
        String searchText = "남";
        AddressSearchRequestDto request = AddressSearchRequestDto.of(searchText, 0, 10);

        // when
        Slice<LegalAddressSearchQueryResponseDto> result = addressSearchService.searchByText(request);

        //then
        assertThat(result.getNumberOfElements()).isEqualTo(3);
        assertThat(result.getContent()).extracting(LegalAddressSearchQueryResponseDto::getFullAddress)
                .containsExactly(
                        "인천광역시 남동구 구월동",
                        "인천광역시 남동구 간석동",
                        "인천광역시 남동구 만수동"
                );
    }

    @Test
    @DisplayName("위험한 검색어 입력은 필터링되어 안전한 토큰만으로 검색된다.")
    void shouldFilterDangerousSearchInput(){
        //given
        String searchText = "%인천% and 1=1 and select * from user";
        int page = 0;
        int size = 10;
        AddressSearchRequestDto request = AddressSearchRequestDto.of(searchText, page, size);

        // when
        Slice<LegalAddressSearchQueryResponseDto> result = addressSearchService.searchByText(request);

        //then
        ArgumentCaptor<OrderedTokens> captor = ArgumentCaptor.forClass(OrderedTokens.class);
        verify(searchRepository, times(1)).searchFullAddress(captor.capture(), eq(PageRequest.of(page, size)));
        OrderedTokens passed = captor.getValue();
        assertThat(passed.concatTokensWithDelimiter(" ")).isEqualTo("인천 11");
        assertThat(result.getNumberOfElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("주어진 검색어가 빈 문자열이면 예외를 던진다")
    void shouldThrowExWhenSearchTextIsBlank(){
        //given
        String searchText =  "  ";
        int page = 0;
        int size = 10;
        AddressSearchRequestDto request = AddressSearchRequestDto.of(searchText, page, size);

        // when & then
        assertThatThrownBy(() -> addressSearchService.searchByText(request))
                .isInstanceOf(BusinessException.class);
    }

}