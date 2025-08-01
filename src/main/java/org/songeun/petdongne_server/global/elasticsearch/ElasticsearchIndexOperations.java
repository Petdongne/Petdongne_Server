package org.songeun.petdongne_server.global.elasticsearch;

import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

public interface ElasticsearchIndexOperations {

    boolean createIndex(IndexCoordinates indexCoordinates);

    boolean deleteIndex(IndexCoordinates indexCoords);

    boolean existIndex(IndexCoordinates indexCoords);

}
