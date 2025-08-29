package org.songeun.petdongne_server.compare.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.compare.application.service.AddressSearchService;
import org.songeun.petdongne_server.compare.domain.entity.Address;
import org.songeun.petdongne_server.compare.domain.entity.AddressTableMetaData;
import org.songeun.petdongne_server.compare.infrastructure.repository.AddressRepository;
import org.songeun.petdongne_server.compare.infrastructure.repository.AddressSearchResponse;
import org.songeun.petdongne_server.compare.infrastructure.schema.AddressSchemaManager;
import org.songeun.petdongne_server.compare.application.dto.AddressSearchRequestDto;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.songeun.petdongne_server.compare.fixture.AddressFixtureFactory.*;

// todo postgre 상속
public class AddressSearchTest extends IntegrationTestSupport {

    @Autowired
    private AddressSearchService searchService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressSchemaManager schemaManager;

    @BeforeEach
    void setUp() throws SQLException {
        String tableName = getAddressTestTableName();
        schemaManager.createGinExtensionIfNotExist();
        schemaManager.createTableIfNotExist(tableName);
        schemaManager.createGinIndex(tableName, "full_address_gin_idx", AddressTableMetaData.fullAddressColumnName());
        schemaManager.createGinIndex(tableName, "address_initials_gin_idx", AddressTableMetaData.addressInitialsColumnName());
        schemaManager.createOrReplaceView(tableName);
    }

    @AfterEach
    void tearDown() {
        schemaManager.dropTableCascadeIfExist();
    }

    private String getAddressTestTableName() {
        return "address_test";
    }

    @Test
    @DisplayName("한 글자 입력 시 해당 글자로 시작하는 주소를 검색한다.")
    void shouldSearchOneCharForInitials(){
        //given
        String searchText = "구";
        int page = 0;
        int size = 10;
        AddressSearchRequestDto requestDto = AddressSearchRequestDto.of(searchText, page, size);

        List<Address> seoulFixture = getSeoulFixture();
        addressRepository.batchInsert(seoulFixture);

        //when
        Slice<AddressSearchResponse> searched = searchService.search(requestDto);

        //then
        assertThat(searched).extracting(AddressSearchResponse::fullAddress)
                .containsExactly(
                        "서울특별시 구로구",
                        "서울특별시 종로구 구기동",
                        "서울특별시 광진구 구의동",
                        "서울특별시 은평구 구산동",
                        "서울특별시 마포구 구수동"
                );
    }

    @Test
    @DisplayName("두 글자 이상 입력 시 이를 모두 포함하는 주소를 검색한다.")
    void shouldSearch(){
        //given
        List<Address> mapogu = getMapoguFixture();
        addressRepository.batchInsert(mapogu);
        String searchText = "마포구";
        int page = 0;
        int size = 10;
        AddressSearchRequestDto requestDto = AddressSearchRequestDto.of(searchText, page, size);

        //when
        Slice<AddressSearchResponse> searched = searchService.search(requestDto);

        //then
        assertThat(searched.getSize()).isEqualTo(size);
        assertThat(searched).extracting(AddressSearchResponse::fullAddress)
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
    @DisplayName("공백 입력 시 빈 검색 결과를 반환한다.")
    void shouldReturnEmptyContentWhenEmptySearchText(){
        //given
        String searchText = "                 ";
        int page = 0;
        int size = 10;
        AddressSearchRequestDto requestDto = AddressSearchRequestDto.of(searchText, page, size);

        //when
        Slice<AddressSearchResponse> searched = searchService.search(requestDto);

        //then
        assertThat(searched).isEmpty();
        assertThat(searched.getNumberOfElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("허용되지 않는 문자 입력 시 이를 제외하고 검색한다.")
    void shouldFilterForSearchText(){
        //given
        String searchText = "I want to search '서울' ):D ";
        int page = 0;
        int size = 10;
        AddressSearchRequestDto requestDto = AddressSearchRequestDto.of(searchText, page, size);

        List<Address> seoulFixture = getSeoulFixture();
        addressRepository.batchInsert(seoulFixture);

        //when
        Slice<AddressSearchResponse> searched = searchService.search(requestDto);

        //then
        assertThat(searched).isNotEmpty();
        assertThat(searched.getNumberOfElements()).isEqualTo(size);
        assertThat(searched).extracting(AddressSearchResponse::fullAddress)
                .containsExactly(
                        "서울특별시",
                        "서울특별시 중구",
                        "서울특별시 서초구",
                        "서울특별시 종로구",
                        "서울특별시 용산구",
                        "서울특별시 성동구",
                        "서울특별시 광진구",
                        "서울특별시 중랑구",
                        "서울특별시 성북구",
                        "서울특별시 구로구"
                );
    }

    @Test
    @DisplayName("시도 축약어 입력 시 같은 뜻을 가지는 주소로 변환하여 검색한다.")
    void shouldSearchWithReplaces(){
        //given
        String searchText = "광주시";
        int page = 0;
        int size = 10;
        AddressSearchRequestDto requestDto = AddressSearchRequestDto.of(searchText, page, size);

        List<Address> allGwangjuAddresses = Stream.of(getGwangJuSiFixture(), getGwangJuMetropolitanCityFixture())
                .flatMap(Collection::stream)
                .toList();
        addressRepository.batchInsert(allGwangjuAddresses);

        //when
        Slice<AddressSearchResponse> searched = searchService.search(requestDto);

        //then
        assertThat(searched).isNotEmpty();
        assertThat(searched).extracting(AddressSearchResponse::fullAddress)
                .containsAnyOf("광주광역시")
                .containsAnyOf( "경기도 광주시");
    }

}
