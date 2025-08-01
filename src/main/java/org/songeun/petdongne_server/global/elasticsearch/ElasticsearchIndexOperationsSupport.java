package org.songeun.petdongne_server.global.elasticsearch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.index.Settings;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchIndexOperationsSupport {

    private final ElasticsearchOperations operations;

    public boolean createIndex(IndexCoordinates indexCoordinates, Class<?> clazz) {
        try {
            IndexOperations indexOperations = operations.indexOps(indexCoordinates);
            Document mapping = indexOperations.createMapping(clazz);
            Settings settings = indexOperations.createSettings(clazz);

            return indexOperations.create(settings, mapping);
        } catch (Exception e) {
             log.warn("Failed to create index {}: {}", indexCoordinates.getIndexName(), e.getMessage());
            return false;
        }
    }

    public boolean deleteIndex(IndexCoordinates indexCoords) {
        return operations.indexOps(indexCoords).delete();
    }

    public boolean existIndex(IndexCoordinates indexCoords) {
        return operations.indexOps(indexCoords).exists();
    }

}
