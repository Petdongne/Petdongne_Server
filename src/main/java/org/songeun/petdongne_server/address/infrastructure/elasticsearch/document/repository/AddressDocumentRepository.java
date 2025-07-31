package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.repository;

import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.List;

/**
 * Spring Data Elasticsearch의 메서드 기반 Repository가
 * 단순한 Document CRUD 기능만 수행하도록 제한하기 위해 정의한 인터페이스입니다.
 *
 */
public interface AddressDocumentRepository {

    boolean saveAll(List<AddressDocument> addressDocuments, IndexCoordinates indexName);

    boolean saveAll(List<AddressDocument> addresses);

    void deleteAll();

}
