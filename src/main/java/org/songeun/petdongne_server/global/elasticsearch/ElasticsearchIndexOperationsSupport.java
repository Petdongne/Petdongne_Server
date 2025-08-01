package org.songeun.petdongne_server.global.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.index.Settings;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElasticsearchIndexOperationsSupport {

    private final ElasticsearchOperations operations;

    public boolean createIndex(IndexCoordinates indexCoordinates, Class<?> clazz) {
        IndexOperations indexOperations = operations.indexOps(indexCoordinates);
        Document mapping = indexOperations.createMapping(clazz);
        Settings settings = indexOperations.createSettings(clazz);

        return indexOperations.create(settings, mapping);
    }

    public boolean deleteIndex(IndexCoordinates indexCoords) {
        return operations.indexOps(indexCoords).delete();
    }

    public boolean existIndex(IndexCoordinates indexCoords) {
        return operations.indexOps(indexCoords).exists();
    }

}
