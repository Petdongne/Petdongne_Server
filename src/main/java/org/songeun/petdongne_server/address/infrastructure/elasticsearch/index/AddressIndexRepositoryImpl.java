package org.songeun.petdongne_server.address.infrastructure.elasticsearch.index;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.index.AliasAction;
import org.springframework.data.elasticsearch.core.index.AliasActionParameters;
import org.springframework.data.elasticsearch.core.index.AliasActions;
import org.springframework.data.elasticsearch.core.index.Settings;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class AddressIndexRepositoryImpl implements AddressIndexRepository {

    private final ElasticsearchOperations operations;

    @Override
    public boolean createIndex(IndexCoordinates indexCoordinates) {
        IndexOperations indexOperations = operations.indexOps(indexCoordinates);
        Document mapping = indexOperations.createMapping(AddressDocument.class);
        Settings settings = indexOperations.createSettings(AddressDocument.class);

        return indexOperations.create(settings, mapping);
    }

    @Override
    public boolean setAlias(IndexCoordinates targetCoords) {
        IndexOperations indexOperations = operations.indexOps(targetCoords);
        IndexCoordinates aliasCoords = operations.getIndexCoordinatesFor(AddressDocument.class);
        AliasActions aliasActions = createAliasActions(indexOperations, aliasCoords);

        return indexOperations.alias(aliasActions);
    }

    private AliasActions createAliasActions(IndexOperations indexOperations, IndexCoordinates aliasCoordinates) {
        AliasActions aliasActions = new AliasActions();
        aliasActions.add(new AliasAction.Add(
                AliasActionParameters.builder()
                        .withIndices(indexOperations.getIndexCoordinates().getIndexNames())
                        .withAliases(aliasCoordinates.getIndexName())
                        .build()
        ));

        return aliasActions;
    }

    @Override
    public Set<String> getIndexNamesByAlias() {
        IndexCoordinates aliasCoords = operations.getIndexCoordinatesFor(AddressDocument.class);

        IndexOperations indexOperations = operations.indexOps(aliasCoords);
        return indexOperations.getAliasesForIndex(aliasCoords.getIndexName()).keySet();
    }

    @Override
    public boolean existIndexByAlias() {
        return !getIndexNamesByAlias().isEmpty();
    }

    @Override
    public boolean existIndex(IndexCoordinates indexCoords) {
        return operations.indexOps(indexCoords).exists();
    }


    @Override
    public boolean createIndex() {
        return getIndexOps().createWithMapping();
    }

    @Override
    public boolean deleteIndex(IndexCoordinates indexCoords) {
        return operations.indexOps(indexCoords).delete();
    }

    private IndexOperations getIndexOps() {
        return operations.indexOps(AddressDocument.class);
    }

}
