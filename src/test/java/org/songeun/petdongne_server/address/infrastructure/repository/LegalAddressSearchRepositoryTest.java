package org.songeun.petdongne_server.address.infrastructure.repository;

import org.junit.jupiter.api.*;
import org.songeun.petdongne_server.address.domain.LegalAddress;
import org.songeun.petdongne_server.address.domain.RegionAddressLevel;
import org.songeun.petdongne_server.address.fixture.LegalAddressFixture;
import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressBoundsSearchQueryResponseDto;
import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressSearchQueryResponseDto;
import org.songeun.petdongne_server.global.search.OrderedTokens;
import org.songeun.petdongne_server.global.search.Token;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LegalAddressSearchRepositoryTest extends PostgresSQLIntegrationTestSupport {

    @Autowired
    private LegalAddressSearchRepository searchRepository;

    @Autowired
    private LegalAddressCoreRepository coreRepository;

    public static final double LATITUDE = 37.541;
    public static final double LONGITUDE = 126.986;

    private final List<LegalAddress> fixture = LegalAddressFixture.createMapoguLegalAddress(LATITUDE, LONGITUDE);

    @BeforeAll
    void beforeAll() {
        coreRepository.saveAll(fixture);
    }

    @AfterAll
    void afterAll() {
        coreRepository.deleteAll(fixture);
    }

    @Test
    @DisplayName("주어진 토큰을 모두 포함하는 법정동 주소를 검색한 후 유사도 순으로 반환한다.")
    void shouldSearchAddressesContainingAllTokens(){
        //given
        OrderedTokens tokens = OrderedTokens.create(new String[]{"서울특별시", "마포구"});
        int pageNumber = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        //when
        Slice<LegalAddressSearchQueryResponseDto> result = searchRepository.searchFullAddress(tokens, pageRequest);

        //then
        assertThat(result.getNumberOfElements()).isEqualTo(pageSize);
        assertThat(result.hasNext()).isTrue();
        assertThat(result).extracting(LegalAddressSearchQueryResponseDto::getFullAddress)
                .containsExactly(
                        "서울특별시 마포구",
                        "서울특별시 마포구 마포동",
                        "서울특별시 마포구 서교동",
                        "서울특별시 마포구 중동",
                        "서울특별시 마포구 서강동",
                        "서울특별시 마포구 아현동",
                        "서울특별시 마포구 공덕동",
                        "서울특별시 마포구 도화동",
                        "서울특별시 마포구 용강동",
                        "서울특별시 마포구 토정동"
                );
    }

    @Test
    @DisplayName("주어진 토큰이 null일 때 빈 검색 결과를 반환한다.")
    void shouldReturnEmptyResultWhenOrderedTokensNull(){
        //given
        OrderedTokens tokens = null;
        int pageNumber = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        //when
        Slice<LegalAddressSearchQueryResponseDto> result = searchRepository.searchFullAddress(tokens, pageRequest);

        //then
        assertThat(result.getNumberOfElements()).isEqualTo(0);
        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("주어진 토큰으로 시작하는 법정동 주소를 검색한 후 유사도 순으로 반환한다.")
    void shouldSearchAddressesStartingWithToken(){
        //given
        Token token = Token.create("마");
        int pageNumber = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        //when
        Slice<LegalAddressSearchQueryResponseDto> result = searchRepository.searchAddressInitials(token, pageRequest);

        //then
        assertThat(result.getNumberOfElements()).isEqualTo(pageSize);
        assertThat(result.hasNext()).isTrue();
        assertThat(result).extracting(LegalAddressSearchQueryResponseDto::getFullAddress)
                .containsExactly(
                        "서울특별시 마포구",
                        "서울특별시 마포구 마포동",
                        "서울특별시 마포구 서교동",
                        "서울특별시 마포구 중동",
                        "서울특별시 마포구 서강동",
                        "서울특별시 마포구 아현동",
                        "서울특별시 마포구 공덕동",
                        "서울특별시 마포구 도화동",
                        "서울특별시 마포구 용강동",
                        "서울특별시 마포구 토정동"
                );
    }

    @Test
    @DisplayName("주어진 토큰이 null일 때 빈 검색 결과를 반환한다.")
    void shouldReturnEmptyResultWhenTokenNull(){
        //given
        Token token = null;
        int pageNumber = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        //when
        Slice<LegalAddressSearchQueryResponseDto> result = searchRepository.searchAddressInitials(token, pageRequest);

        //then
        assertThat(result.getNumberOfElements()).isEqualTo(0);
        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("경계 내에 존재하는 특정 단계의 법정동 주소를 찾는다")
    void test(){
        //given
        Double minLon = LONGITUDE;
        Double minLat = LATITUDE;
        Double maxLon = LONGITUDE + 0.1;
        Double maxLat = LATITUDE + 0.1;
        RegionAddressLevel addressLevel = RegionAddressLevel.EMD;

        //when
        List<LegalAddressBoundsSearchQueryResponseDto> result = searchRepository.findAddressWithinBounds(
                minLon, minLat, maxLon, maxLat, addressLevel);

        //then
        List<LegalAddress> filteredFixture = fixture.stream()
                .filter(legalAddress -> legalAddress.getRegionAddressLevel().equals(RegionAddressLevel.EMD))
                .toList();

        assertThat(result).hasSize(filteredFixture.size());
        assertThat(result).extracting("fullAddress", "latitude", "longitude", "regionLevel")
                            .containsExactlyInAnyOrderElementsOf(
                                    filteredFixture.stream()
                                            .map(address -> Tuple.tuple(
                                                    address.getFullAddress(), address.getLatitude(),
                                                    address.getLongitude(), address.getRegionAddressLevel()))
                                            .toList()
                            );
    }

}