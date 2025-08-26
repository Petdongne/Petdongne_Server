package org.songeun.petdongne_server.compare.infrastructure.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.compare.domain.Address;
import org.songeun.petdongne_server.compare.domain.AddressTableMetaData;
import org.songeun.petdongne_server.compare.fixture.AddressFixtureFactory;
import org.songeun.petdongne_server.compare.infrastructure.schema.AddressSchemaManager;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressSearchRepositoryTest extends IntegrationTestSupport { // todo postgre로 변경

    @Autowired
    private AddressSearchRepository searchRepository;

    @Autowired
    private AddressInsertRepository insertRepository;

    @Autowired
    private AddressSchemaManager schemaManager;

    @BeforeEach
    void setUp() throws SQLException {
        String tableName = "address_test";
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

    @Test
    @DisplayName("검색어를 포함하는 주소를 유사도 순으로 찾는다.")
    void shouldSearchForAddress(){
        //given
        List<String> query = List.of("서울특별시", "마포구");
        int pageNumber = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        List<Address> mapoguFixture = AddressFixtureFactory.getMapoguFixture();
        insertRepository.batchInsert(mapoguFixture);

        //when
        Slice<AddressSearchResponse> result = searchRepository.searchForAddress(query, pageRequest);

        //then
        assertThat(result.getSize()).isEqualTo(pageSize);
        assertThat(result.getNumberOfElements()).isEqualTo(pageSize);
        assertThat(result.hasNext()).isTrue();
        assertThat(result).extracting(AddressSearchResponse::fullAddress)
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
    @DisplayName("검색어와 주소 각 부분의 시작 글자가 같은 주소를 찾는다.")
    void shouldSearchForAddressInitials(){
        //given
        String query = "마";
        int pageNumber = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        List<Address> mapoguFixture = AddressFixtureFactory.getMapoguFixture();
        insertRepository.batchInsert(mapoguFixture);

        //when
        Slice<AddressSearchResponse> result = searchRepository.searchForAddressInitials(query, pageRequest);

        //then
        assertThat(result.getSize()).isEqualTo(pageSize);
        assertThat(result.getNumberOfElements()).isEqualTo(pageSize);
        assertThat(result.hasNext()).isTrue();
        assertThat(result).extracting(AddressSearchResponse::fullAddress)
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
    @DisplayName("검색어가 두 글자 이상이면 예외를 던진다.")
    void shouldReturnEmptyResultWhenBlankSearchQuery(){
        //given
        String query = "검색";
        int pageNumber = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        List<Address> mapoguFixture = AddressFixtureFactory.getMapoguFixture();
        insertRepository.batchInsert(mapoguFixture);

        //when & then
        assertThatThrownBy(() ->searchRepository.searchForAddressInitials(query, pageRequest))
                .isInstanceOf(RuntimeException.class);
    }

}