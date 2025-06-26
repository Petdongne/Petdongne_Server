package org.songeun.petdongne_server.address.infrastructure.elasticsearch.search;

import org.junit.jupiter.api.*;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.repository.AddressDocumentRepository;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument.FieldConstants.HIERARCHY_LEVEL;
import static org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument.FieldConstants.SCORE;

// TODO: 통합 테스트 모듈 분리
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Testcontainers
class AddressSearchRepositoryTest {

/*    @Container
    static ElasticsearchContainer elasticsearchContainer =
            new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.17.10")
                    .withReuse(true);

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress);
    }*/

    @Autowired
    private AddressSearchRepository searchRepository;

    @Autowired
    private AddressDocumentRepository documentRepository;

    @Autowired
    private AddressIndexRepository addressIndexRepository;

    @BeforeAll
    void beforeAll() {
        if (!addressIndexRepository.existIndex()) {
            addressIndexRepository.createIndex();
        }
    }

    @AfterEach
    void tearDown() {
        documentRepository.deleteAll();
    }

    @Test
    @DisplayName("검색어가 포함된 주소를 주어진 정렬 순서대로 반환한다.")
    void shouldReturnAddressesInHierarchicalOrder() {
        //given
        var addressDocuments = createAddressDocuments(
                "경상남도",
                "경상남도 진주시",
                "경상남도 진주시 일반성면",
                "경상남도 진주시 일반성면 창촌리"
        );
        documentRepository.bulkSave(addressDocuments);

        String query = "경상남도";
        Sort sort = Sort.by(
                Sort.Order.by(HIERARCHY_LEVEL),
                Sort.Order.desc(SCORE)
        );

        //when
        var searchHits = searchRepository.searchAddress(query, PageRequest.ofSize(addressDocuments.size()), sort);

        //then
        assertThat(extractDocuments(searchHits)).hasSize(addressDocuments.size())
                .extracting("fullAddress")
                .containsExactly(
                        "경상남도",
                        "경상남도 진주시",
                        "경상남도 진주시 일반성면",
                        "경상남도 진주시 일반성면 창촌리"
                );
    }

    @Test
    @DisplayName("한 글자 검색어로 주소를 검색할 수 있다.")
    void shouldSearchWithSingleCharacter() {
        //given
        var addressDocuments = createAddressDocuments(
                "경상남도",
                "경상남도 진주시",
                "경상남도 진주시 일반성면",
                "경상남도 진주시 일반성면 창촌리"
        );
        documentRepository.bulkSave(addressDocuments);

        String query = "경";
        Sort sort = Sort.by(
                Sort.Order.by(HIERARCHY_LEVEL),
                Sort.Order.desc(SCORE)
        );

        //when
        var searchHits = searchRepository.searchAddress(query, PageRequest.ofSize(addressDocuments.size()), sort);

        //then
        assertThat(extractDocuments(searchHits)).hasSize(addressDocuments.size())
                .extracting("fullAddress")
                .containsExactly(
                        "경상남도",
                        "경상남도 진주시",
                        "경상남도 진주시 일반성면",
                        "경상남도 진주시 일반성면 창촌리"
                );
    }

    // 예시: '서울' 검색어로 '서울특별시' 검색 가능
    @Test
    @DisplayName("부분 검색어로 주소를 검색할 수 있다.")
    void shouldSearchWithPartialQuery(){
        //given
        var addressDocuments = createAddressDocuments(
                "서울특별시",
                "서울특별시 종로구",
                "서울특별시 중구",
                "서울특별시 용산구",
                "서울특별시 종로구 청운동",
                "서울특별시 종로구 신교동",
                "서울특별시 용산구 동자동"
        );
        documentRepository.bulkSave(addressDocuments);

        String query = "서울";
        Sort sort = Sort.by(
                Sort.Order.by(HIERARCHY_LEVEL),
                Sort.Order.desc(SCORE)
        );

        //when
        var searchHits = searchRepository.searchAddress(query, PageRequest.ofSize(addressDocuments.size()), sort);

        //then
        assertThat(extractDocuments(searchHits)).hasSize(addressDocuments.size())
                .extracting("fullAddress")
                .containsExactlyInAnyOrder(
                        "서울특별시",
                        "서울특별시 중구",
                        "서울특별시 종로구",
                        "서울특별시 용산구",
                        "서울특별시 종로구 청운동",
                        "서울특별시 종로구 신교동",
                        "서울특별시 용산구 동자동"
                );
    }

    // 예시: '논현동 인천' 검색어로 '인천 남동구 논현동' 검색 가능
    @Test
    @DisplayName("주소 구성 요소의 순서가 바뀌어도 검색할 수 있다.")
    void shouldSearchWithDifferentWordOrder(){
        //given
        var addressDocuments = createAddressDocuments(
                "인천광역시 남동구",
                "인천광역시 남동구 논현동",
                "서울특별시 강남구 논현동"
        );
        documentRepository.bulkSave(addressDocuments);

        String query = "논현동 인천";
        Sort sort = Sort.by(
                Sort.Order.by(HIERARCHY_LEVEL),
                Sort.Order.desc(SCORE)
        );

        //when
        var searchHits = searchRepository.searchAddress(query, PageRequest.ofSize(addressDocuments.size()), sort);

        //then
        assertThat(extractDocuments(searchHits)).hasSize(1)
                .extracting("fullAddress")
                .containsExactly(
                        "인천광역시 남동구 논현동"
                );
    }

    // 예시: '경남' 검색어로 '경상남도'로 검색 가능
    @Test
    @DisplayName("시도 축약어로 주소를 검색할 수 있다.")
    void shouldSearchWithAbbreviatedSido(){
        //given
        var addressDocs = createAddressDocuments(
                "경상남도",
                "경상남도 진주시",
                "경상남도 진주시 일반성면",
                "경상남도 진주시 일반성면 창촌리"
        );
        documentRepository.bulkSave(addressDocs);

        String query = "경남 진주시 일반성면";
        Sort sort = Sort.by(
                Sort.Order.by(HIERARCHY_LEVEL),
                Sort.Order.desc(SCORE)
        );

        //when
        var searchHits = searchRepository.searchAddress(query, PageRequest.ofSize(addressDocs.size()), sort);

        //then
        assertThat(extractDocuments(searchHits)).hasSize(2)
                .extracting("fullAddress")
                .containsExactly(
                        "경상남도 진주시 일반성면",
                        "경상남도 진주시 일반성면 창촌리"
                );
    }


    // 예시: '광교' → '상광교동', '하광교동' 검색 가능
    @Test
    @DisplayName("중간 문자열로 주소를 검색할 수 있다.")
    void shouldSearchWithSubstring(){
        //given
        var addressDocs = createAddressDocuments(
                "경기도 수원시 장안구 상광교동",
                "경기도 수원시 장안구 하광교동"
        );
        documentRepository.bulkSave(addressDocs);

        String query = "광교";
        Sort sort = Sort.by(
                Sort.Order.by(HIERARCHY_LEVEL),
                Sort.Order.desc(SCORE)
        );

        //when
        var searchHits = searchRepository.searchAddress(query, PageRequest.ofSize(addressDocs.size()), sort);

        //then
        assertThat(extractDocuments(searchHits)).hasSize(addressDocs.size())
                .extracting("fullAddress")
                .containsExactlyInAnyOrder(
                        "경기도 수원시 장안구 상광교동",
                        "경기도 수원시 장안구 하광교동"
                );
    }

    @Test
    @DisplayName("광주시를 입력했을 때 광주광역시와 경기도 광주시를 검색할 수 있다.")
    void shouldReturnBothGwangjuCities(){
        //given
        var addressDocs = createAddressDocuments(
                "광주광역시",
                "광주광역시 동구",
                "광주광역시 서구",
                "경기도 광주시",
                "경기도 광주시 삼동",
                "경기도 광주시 직동"
        );
        documentRepository.bulkSave(addressDocs);

        String query = "광주시";
        Sort sort = Sort.by(
                Sort.Order.by(HIERARCHY_LEVEL),
                Sort.Order.desc(SCORE)
        );

        //when
        var searchHits = searchRepository.searchAddress(query, PageRequest.ofSize(addressDocs.size()), sort);

        //then
        assertThat(extractDocuments(searchHits)).hasSize(addressDocs.size())
                .extracting("fullAddress")
                .containsExactlyInAnyOrder(
                        "광주광역시",
                        "광주광역시 동구",
                        "광주광역시 서구",
                        "경기도 광주시",
                        "경기도 광주시 삼동",
                        "경기도 광주시 직동"
                );
    }

    @Test
    @DisplayName("광주광역시를 입력했을 때 경기도 광주시는 검색되지 않아야 한다.")
    void shouldExcludeGyeonggiGwangjusi(){
        //given
        var addressDocuments = createAddressDocuments(
                "광주광역시",
                "광주광역시 동구",
                "광주광역시 서구",
                "경기도 광주시",
                "경기도 광주시 삼동",
                "경기도 광주시 직동"
        );
        documentRepository.bulkSave(addressDocuments);

        String query = "광주광역시";
        Sort sort = Sort.by(
                Sort.Order.by(HIERARCHY_LEVEL),
                Sort.Order.desc(SCORE)
        );

        //when
        var searchHits = searchRepository.searchAddress(query, PageRequest.ofSize(addressDocuments.size()), sort);

        //then
        assertThat(extractDocuments(searchHits)).hasSize(3)
                .extracting("fullAddress")
                .containsExactlyInAnyOrder(
                        "광주광역시",
                        "광주광역시 동구",
                        "광주광역시 서구"
                );
    }


    private List<AddressDocument> extractDocuments(SearchPage<AddressDocument> hits) {
        if (hits.getSize() == 0){
            return new ArrayList<>();
        }

        return hits.getContent().stream()
                .map(SearchHit::getContent)
                .toList();
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