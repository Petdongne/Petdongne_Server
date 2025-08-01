package org.songeun.petdongne_server.address.infrastructure.elasticsearch.index;

import org.junit.jupiter.api.*;
import org.songeun.petdongne_server.address.support.ElasticsearchIntegrationTestSupport;
import org.songeun.petdongne_server.address.support.LocalElasticsearchIntegrationTestSupport;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddressIndexOperationsImplTest extends ElasticsearchIntegrationTestSupport {

    @BeforeAll
    void beforeAll() {
        cleanupExistingIndexes();
    }

    @AfterEach
    void tearDown() {
        cleanupExistingIndexes();
    }

    @Test
    @DisplayName("인덱스를 성공적으로 생성하면 true를 반환한다.")
    void shouldReturnTrueWhenCreateIndexSuccessfully() {
        //given
        IndexCoordinates indexCoordinates = createIndexCoords();

        //when
        boolean result = indexOperations.createIndex(indexCoordinates);

        //then
        assertThat(result).isTrue();
        assertThat(indexOperations.existIndex(indexCoordinates)).isTrue();
    }

    @Test
    @DisplayName("주소 인덱스의 별칭이 존재하면 true를 반환한다.")
    void shouldReturnTrueWhenAliasExists() {
        //given
        IndexCoordinates indexCoordinates = createIndexCoords();
        indexOperations.createIndex(indexCoordinates);
        indexOperations.setAlias(indexCoordinates);

        //when
        boolean result = indexOperations.existAlias();

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("주소 인덱스의 별칭이 존재하지 않으면 false를 반환한다.")
    void shouldReturnFalseWhenAliasNotExists() {
        //given

        //when
        boolean result = indexOperations.existAlias();

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("주어진 인덱스에 별칭을 지정한다.")
    void shouldSetAliasToGivenIndex() {
        //given
        IndexCoordinates indexCoordinates = createIndexCoords();
        indexOperations.createIndex(indexCoordinates);

        //when
        boolean result = indexOperations.setAlias(indexCoordinates);

        //then
        assertThat(result).isTrue();
        assertThat(indexOperations.existAlias()).isTrue();
        assertThat(indexOperations.findAliasTargetIndexNames()).contains(indexCoordinates.getIndexName());
    }

    @Test
    @DisplayName("별칭이 가리키는 인덱스의 이름을 조회한다.")
    void shouldReturnAliasTargetIndexNames() {
        //given
        IndexCoordinates indexCoordinates = createIndexCoords();
        indexOperations.createIndex(indexCoordinates);
        indexOperations.setAlias(indexCoordinates);

        //when
        var indexNames = indexOperations.findAliasTargetIndexNames();

        //then
        assertThat(indexNames).isNotEmpty();
        assertThat(indexNames).contains(indexCoordinates.getIndexName());
    }

    @Test
    @DisplayName("인덱스를 삭제한다.")
    void shouldDeleteIndexSuccessfully() {
        //given
        IndexCoordinates indexCoordinates = createIndexCoords();
        indexOperations.createIndex(indexCoordinates);

        //when
        boolean result = indexOperations.deleteIndex(indexCoordinates);

        //then
        assertThat(result).isTrue();
        assertThat(indexOperations.existIndex(indexCoordinates)).isFalse();
    }

    @Test
    @DisplayName("인덱스가 존재하면 true를 반환한다.")
    void shouldReturnTrueWhenSpecificIndexExists() {
        //given
        IndexCoordinates indexCoordinates = createIndexCoords();
        indexOperations.createIndex(indexCoordinates);

        //when
        boolean result = indexOperations.existIndex(indexCoordinates);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인덱스가 존재하지 않으면 false를 반환한다.")
    void shouldReturnFalseWhenSpecificIndexNotExists() {
        //given
        IndexCoordinates indexCoordinates = createIndexCoords();

        //when
        boolean result = indexOperations.existIndex(indexCoordinates);

        //then
        assertThat(result).isFalse();
    }

    private IndexCoordinates createIndexCoords() {
        String indexName = UUID.randomUUID().toString().substring(0, 6);
        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
        return indexCoordinates;
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