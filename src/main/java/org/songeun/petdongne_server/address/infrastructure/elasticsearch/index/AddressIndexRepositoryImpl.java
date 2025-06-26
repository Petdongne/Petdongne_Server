package org.songeun.petdongne_server.address.infrastructure.elasticsearch.index;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class AddressIndexRepositoryImpl implements AddressIndexRepository {

    private final ElasticsearchOperations operations;

    @Override
    public boolean existIndex() {
        return getIndexOps().exists();
    }

    @Override
    public boolean createIndex() {
        return getIndexOps().createWithMapping();
    }

    private IndexOperations getIndexOps() {
        return operations.indexOps(AddressDocument.class);
    }

}
