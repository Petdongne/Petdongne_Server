package org.songeun.petdongne_server.compare.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressType;
import org.songeun.petdongne_server.compare.application.service.AddressSearchService;
import org.songeun.petdongne_server.compare.domain.Address;
import org.songeun.petdongne_server.compare.domain.AddressTableMetaData;
import org.songeun.petdongne_server.compare.infrastructure.repository.AddressRepository;
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
import static org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressType.*;

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
    @DisplayName("검색 쿼리와 (부분) 일치하는 검색 결과를 유사도 순으로 반환한다.")
    void shouldSearch(){
        //given
        List<Address> mapogu = getMapoguFixture();
        addressRepository.batchInsert(mapogu);
        String searchText = "서울특별시 마포구";
        int page = 0;
        int size = 10;
        AddressSearchRequestDto requestDto = AddressSearchRequestDto.of(searchText, page, size);

        //when
        Slice<Address> searched = searchService.search(requestDto);

        //then
        assertThat(searched.getSize()).isEqualTo(size);
        assertThat(searched).extracting(Address::getFullAddress)
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
    @DisplayName("한 글자 검색은 시도, 시군구, 읍면동 (리) 첫글자를 대상으로 한다.")
    void shouldSearchOneCharForInitials(){
        //given
        String searchText = "구";
        int page = 0;
        int size = 10;
        AddressSearchRequestDto requestDto = AddressSearchRequestDto.of(searchText, page, size);

        List<Address> seoulFixture = getSeoulFixture();
        addressRepository.batchInsert(seoulFixture);

        //when
        Slice<Address> searched = searchService.search(requestDto);

        //then
        assertThat(searched).extracting(Address::getFullAddress)
                .containsExactly(
                        "서울특별시 구로구",
                        "서울특별시 종로구 구기동",
                        "서울특별시 광진구 구의동",
                        "서울특별시 은평구 구산동",
                        "서울특별시 마포구 구수동"
                );
    }


    @Test
    @DisplayName("공백 검색 쿼리 입력 시 빈 검색 결과를 반환한다.")
    void shouldReturnEmptyContentWhenEmptySearchText(){
        //given
        String searchText = "                 ";
        int page = 0;
        int size = 10;
        AddressSearchRequestDto requestDto = AddressSearchRequestDto.of(searchText, page, size);

        //when
        Slice<Address> searched = searchService.search(requestDto);

        //then
        assertThat(searched).isEmpty();
        assertThat(searched.getNumberOfElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("검색 쿼리는 한글, 숫자를 제외하고 필터링된다.")
    void shouldFilterForSearchText(){
        //given
        String searchText = "I want to search '서울' ):D ";
        int page = 0;
        int size = 10;
        AddressSearchRequestDto requestDto = AddressSearchRequestDto.of(searchText, page, size);

        List<Address> seoulFixture = getSeoulFixture();
        addressRepository.batchInsert(seoulFixture);

        //when
        Slice<Address> searched = searchService.search(requestDto);

        //then
        assertThat(searched).isNotEmpty();
        assertThat(searched.getNumberOfElements()).isEqualTo(size);
        assertThat(searched).extracting(Address::getFullAddress)
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
    @DisplayName("시도 축약어로 검색을 수행한다.")
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
        Slice<Address> searched = searchService.search(requestDto);

        //then
        assertThat(searched).isNotEmpty();
        assertThat(searched).extracting(Address::getFullAddress)
                .containsAnyOf("광주광역시")
                .containsAnyOf( "경기도 광주시");
    }

    @Test
    @DisplayName("검색 받은 페이지의 다음 페이지를 조회한다.")
    void shouldSearchNextPage(){
        //given
        List<Address> mapogu = getMapoguFixture();
        addressRepository.batchInsert(mapogu);
        String searchText = "서울특별시 마포구";
        int page = 1;
        int size = 10;
        AddressSearchRequestDto requestDto = AddressSearchRequestDto.of(searchText, page, size);

        //when
        Slice<Address> searched = searchService.search(requestDto);

        //then
        assertThat(searched.getSize()).isEqualTo(size);
        assertThat(searched).extracting(Address::getFullAddress)
                .containsExactly(
                        "서울특별시 마포구 대흥동",
                        "서울특별시 마포구 염리동",
                        "서울특별시 마포구 신수동",
                        "서울특별시 마포구 현석동",
                        "서울특별시 마포구 구수동",
                        "서울특별시 마포구 창전동",
                        "서울특별시 마포구 상수동",
                        "서울특별시 마포구 하중동",
                        "서울특별시 마포구 신정동",
                        "서울특별시 마포구 당인동"
                );
    }

    private List<Address> getMapoguFixture() {
        return List.of(
                Address.create("서울특별시 마포구", "서마", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 마포동", "서마마", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 서교동", "서마서", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 중동", "서마중", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 서강동", "서마서", ADMIN_DONG_ADDRESS),
                Address.create("서울특별시 마포구 아현동", "서마아", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 공덕동", "서마공", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 도화동", "서마도", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 용강동", "서마용", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 토정동", "서마토", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 대흥동", "서마대", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 염리동", "서마염", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 신수동", "서마신", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 현석동", "서마현", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 구수동", "서마구", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 창전동", "서마창", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 상수동", "서마상", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 하중동", "서마하", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 신정동", "서마신", AddressType.LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 당인동", "서마당", AddressType.LEGAL_DONG_ADDRESS)
        );
    }

    public static List<Address> getSeoulFixture() {
        return List.of(
                Address.create("서울특별시", "서", LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 중구", "서중", LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 서초구", "서서", LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 종로구", "서종", LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 용산구", "서용", LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 성동구", "서성", LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 광진구", "서광", LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 중랑구", "서중", LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 성북구", "서성", LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 구로구", "서구", LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 종로구 구기동", "서종구", LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 광진구 구의동", "서광구", LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 은평구 구산동", "서은구", LEGAL_DONG_ADDRESS),
                Address.create("서울특별시 마포구 구수동", "서마구", LEGAL_DONG_ADDRESS)
        );
    }

    public static List<Address> getGwangJuSiFixture() {
        return List.of(
                Address.create("경기도 광주시", "경광", LEGAL_DONG_ADDRESS),
                Address.create("경기도 광주시 경안동", "경광경", LEGAL_DONG_ADDRESS),
                Address.create("경기도 광주시 삼동", "경광삼", LEGAL_DONG_ADDRESS),
                Address.create("경기도 광주시 직동", "경광직", LEGAL_DONG_ADDRESS)
        );
    }

    private static List<Address> getGwangJuMetropolitanCityFixture() {
        return List.of(
                Address.create("광주광역시", "광", LEGAL_DONG_ADDRESS),
                Address.create("광주광역시 동구", "광동", LEGAL_DONG_ADDRESS),
                Address.create("광주광역시 서구", "광서", LEGAL_DONG_ADDRESS),
                Address.create("광주광역시 남구", "광남", LEGAL_DONG_ADDRESS),
                Address.create("광주광역시 북구", "광북", LEGAL_DONG_ADDRESS)
        );
    }

}
