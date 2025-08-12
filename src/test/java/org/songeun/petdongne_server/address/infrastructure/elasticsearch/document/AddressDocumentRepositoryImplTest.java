package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import org.junit.jupiter.api.*;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.repository.AddressDocumentRepository;
import org.songeun.petdongne_server.address.support.ElasticsearchIntegrationTestSupport;
import org.songeun.petdongne_server.address.support.LocalElasticsearchIntegrationTestSupport;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddressDocumentRepositoryImplTest extends LocalElasticsearchIntegrationTestSupport {

    @BeforeAll
    void beforeAll() {
        cleanupExistingIndexes();
    }

    @AfterEach
    void tearDown() {
        cleanupExistingIndexes();
    }

    @Test
    @DisplayName("지정하는 인덱스에 문서를 저장하고 저장된 개수를 반환한다.")
    void shouldReturnTrueWhenSuccessfullySavedForIndex() {
        //given
        IndexCoordinates indexCoordinates = createIndexCoords();
        indexOperations.createIndex(indexCoordinates);
        int documentCount = 3;
        List<AddressDocument> requestDocuments = createAddressDocuments(documentCount);

        //when
        long savedCount = documentRepository.saveAll(requestDocuments, indexCoordinates);

        //then
        assertThat(indexOperations.existIndex(indexCoordinates)).isTrue();
        assertThat(documentCount).isEqualTo(savedCount);
    }

    @Test
    @DisplayName("모든 문서를 삭제한다.")
    void shouldDeleteAllDocuments() {
        //given
        IndexCoordinates indexCoordinates = createIndexCoords();
        indexOperations.createIndex(indexCoordinates);

        int documentCount = 3;
        List<AddressDocument> requestDocuments = createAddressDocuments(documentCount);
        documentRepository.saveAll(requestDocuments);

        //when
//        documentRepository.deleteAll();

        //then
        assertDeletedAll(documentCount, requestDocuments);
    }

    private void assertDeletedAll(int documentCount, List<AddressDocument> requestDocuments) {
        List<AddressDocument> savedDocuments = createAddressDocuments(documentCount);

/*        long savedCount = documentRepository.saveAll(requestDocuments, indexCoordinates);
        assertThat(savedCount).isEqualTo(savedDocuments.size());*/
    }

    private List<AddressDocument> createAddressDocuments(int count) {
        if (count <= 0) return Collections.emptyList();

        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> createAddressDocument("테스트 주소 " + i))
                .toList();
    }

    private AddressDocument createAddressDocument(String fullAddress) {
        AddressDocument document = new AddressDocument();
        ReflectionTestUtils.setField(document, "id", UUID.randomUUID().toString());
        ReflectionTestUtils.setField(document, "code", "TEST" + String.format("%03d", fullAddress.hashCode()));
        ReflectionTestUtils.setField(document, "fullAddress", fullAddress);
        
        String[] parts = fullAddress.split(" ");
        if (parts.length > 0) {
            ReflectionTestUtils.setField(document, "sido", parts[0]);
        }
        if (parts.length > 1) {
            ReflectionTestUtils.setField(document, "sigungu", parts[1]);
        }
        if (parts.length > 2) {
            ReflectionTestUtils.setField(document, "eupmyeondong", parts[2]);
        }
        if (parts.length > 3) {
            ReflectionTestUtils.setField(document, "re", parts[3]);
        }
        
        return document;
    }

    private IndexCoordinates createIndexCoords() {
        String indexName = UUID.randomUUID().toString().substring(0, 6);
        return IndexCoordinates.of(indexName);
    }

    private void cleanupExistingIndexes() {
        var indexNames = indexOperations.findAliasTargetIndexNames();
        if (indexNames != null) {
            for (String indexName : indexNames) {
                System.out.println("Deleting index: " + indexName);
                IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
                indexOperations.deleteIndex(indexCoordinates);
            }
        }
    }

}