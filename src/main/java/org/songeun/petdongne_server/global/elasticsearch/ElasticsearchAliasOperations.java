package org.songeun.petdongne_server.global.elasticsearch;

import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.Set;

public interface ElasticsearchAliasOperations {

    boolean setAlias(IndexCoordinates targetCoords);

    Set<String> findAliasTargetIndexNames();

    boolean existAlias();

}
