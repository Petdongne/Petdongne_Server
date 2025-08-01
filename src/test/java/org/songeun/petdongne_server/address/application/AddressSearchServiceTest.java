package org.songeun.petdongne_server.address.application;

import org.junit.jupiter.api.*;
import org.songeun.petdongne_server.address.application.service.AddressSearchService;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexNameFactory;
import org.songeun.petdongne_server.address.support.ElasticsearchIntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddressSearchServiceTest extends ElasticsearchIntegrationTestSupport {

    @Autowired
    private AddressSearchService addressSearchService;

    @BeforeAll
    void beforeAll() {
        if (!indexOperations.existAlias()) {
            String addressIndexName = AddressIndexNameFactory.createAddressIndexName();
            IndexCoordinates indexCoordinates = IndexCoordinates.of(addressIndexName);
            indexOperations.createIndex(indexCoordinates);
            indexOperations.setAlias(indexCoordinates);
        }
    }

    @AfterEach
    void tearDown() {
        documentRepository.deleteAll();
    }

    @Test
    @DisplayName("검색어와 매칭되는 주소를 찾는다.")
    void shouldSearchAddressUsingSearchText(){
        //given
        var addressDocuments = createAddressDocuments(
                "인천광역시",
                "인천광역시 남동구",
                "인천광역시 서구",
                "인천광역시 연수구",
                "인천광역시 남동구 논현1동",
                "인천광역시 남동구 논현2동"
        );
        documentRepository.saveAll(addressDocuments);

        int requestPageNum = 0;
        PageRequest pageRequest = PageRequest.of(requestPageNum, 10);
        String searchText = "인천시 남동구";

        //when
        Page<AddressDocument> result = addressSearchService.search(searchText, pageRequest);

        //then
        int expectedTotalMatches = 3;
        assertThat(result.getTotalElements()).isEqualTo(expectedTotalMatches);
        assertThat(result.getNumber()).isEqualTo(requestPageNum);
        assertThat(result.getContent()).hasSize(expectedTotalMatches)
                .extracting("fullAddress")
                .containsExactlyInAnyOrder(
                        "인천광역시 남동구",
                        "인천광역시 남동구 논현1동",
                        "인천광역시 남동구 논현2동"
                );
    }

    @Test
    @DisplayName("검색 결과가 없을 때에는 빈 결과를 반환한다.")
    void shouldReturnEmptyWhenNoResult(){
        //given
        String searchText = "양파쿵야의 아파트";
        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        Page<AddressDocument> result = addressSearchService.search(searchText, pageRequest);

        //then
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getContent()).hasSize(0);
        assertThat(result.getNumber()).isEqualTo(0);
    }

    private List<AddressDocument> createAddressDocuments(String ...fullAddresses) {
        return Arrays.stream(fullAddresses)
                .map(this::createAddress)
                .toList();
    }

    private AddressDocument createAddress(String fullAddress) {
        var doc = new AddressDocument();
        ReflectionTestUtils.setField(doc, "fullAddress", fullAddress);
        String[] parts = fullAddress.split(" ");

        if (parts.length > 0)
            ReflectionTestUtils.setField(doc, "sido", parts[0]);

        if (parts.length > 1)
            ReflectionTestUtils.setField(doc, "sigungu", parts[1]);

        if (parts.length > 2)
            ReflectionTestUtils.setField(doc, "eupmyeondong", parts[2]);

        if (parts.length > 3)
            ReflectionTestUtils.setField(doc, "re", parts[3]);

        return doc;
    }

}