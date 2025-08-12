package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.repository;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.StreamSupport;

@Repository
@RequiredArgsConstructor
public class AddressDocumentRepositoryImpl implements AddressDocumentRepository {

    private final ElasticsearchOperations operations;
    private final ElasticsearchAddressRepository elasticsearchDocumentRepository;

    @Override
    public long saveAll(List<AddressDocument> addressDocuments, IndexCoordinates indexCoordinates) {
        Iterable<AddressDocument> saved = operations.save(addressDocuments, indexCoordinates);

        return StreamSupport.stream(saved.spliterator(), false).count();
    }

    @Override
    public long saveAll(List<AddressDocument> addresses) {
        Iterable<AddressDocument> saved = elasticsearchDocumentRepository.saveAll(addresses);

        return StreamSupport.stream(saved.spliterator(), false).count();
    }

    @Override
    public void deleteAll(IndexCoordinates indexCoordinates) {

    }

}
