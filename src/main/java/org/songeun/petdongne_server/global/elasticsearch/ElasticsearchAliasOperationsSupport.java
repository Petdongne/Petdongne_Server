package org.songeun.petdongne_server.global.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.index.AliasAction;
import org.springframework.data.elasticsearch.core.index.AliasActionParameters;
import org.springframework.data.elasticsearch.core.index.AliasActions;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ElasticsearchAliasOperationsSupport {

    private final ElasticsearchOperations operations;

    public boolean setAlias(IndexCoordinates targetCoords, Class<?> clazz) {
        IndexOperations indexOperations = operations.indexOps(targetCoords);
        IndexCoordinates aliasCoords = operations.getIndexCoordinatesFor(clazz);
        AliasActions aliasActions = createAliasActions(indexOperations, aliasCoords);

        return indexOperations.alias(aliasActions);
    }

    @Nullable
    public Set<String> findAliasTargetIndexNames(Class<?> clazz) {
        IndexCoordinates aliasCoords = operations.getIndexCoordinatesFor(clazz);
        IndexOperations indexOperations = operations.indexOps(aliasCoords);

        try {
            return indexOperations.getAliases(aliasCoords.getIndexName()).keySet();
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }

    public boolean existAlias(Class<?> clazz) {
        IndexCoordinates aliasCoords = operations.getIndexCoordinatesFor(clazz);

        return operations.indexOps(aliasCoords).exists();
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

}
