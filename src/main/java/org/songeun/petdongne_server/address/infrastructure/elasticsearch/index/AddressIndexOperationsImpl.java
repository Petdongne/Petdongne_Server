package org.songeun.petdongne_server.address.infrastructure.elasticsearch.index;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.global.elasticsearch.ElasticsearchAliasOperationsSupport;
import org.songeun.petdongne_server.global.elasticsearch.ElasticsearchIndexOperationsSupport;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class AddressIndexOperationsImpl implements AddressIndexOperations {

    private final ElasticsearchAliasOperationsSupport aliasOperationsSupport;
    private final ElasticsearchIndexOperationsSupport indexOperationsSupport;

    private static final Class<AddressDocument> clazz = AddressDocument.class;

    @Override
    public boolean createIndex(IndexCoordinates indexCoordinates) {
        return indexOperationsSupport.createIndex(indexCoordinates, clazz);
    }

    @Override
    public boolean setAlias(IndexCoordinates targetCoords) {
        return aliasOperationsSupport.setAlias(targetCoords, clazz);
    }

    @Override
    public Set<String> findAliasTargetIndexNames() {
        return aliasOperationsSupport.findAliasTargetIndexNames(clazz);
    }

    @Override
    public boolean existAlias() {
        return aliasOperationsSupport.existAlias(clazz);
    }

    @Override
    public boolean existIndex(IndexCoordinates indexCoords) {
        return indexOperationsSupport.existIndex(indexCoords);
    }

    @Override
    public boolean deleteIndex(IndexCoordinates indexCoords) {
        return indexOperationsSupport.deleteIndex(indexCoords);
    }

}
