package org.songeun.petdongne_server.address.infrastructure.elasticsearch.index;

import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.Set;

public interface AddressIndexRepository {

    boolean createIndex(IndexCoordinates indexCoordinates);

    boolean setAlias(IndexCoordinates indexNameWrapper);

    Set<String> getIndexNamesByAlias();

    boolean existIndexByAlias();

    boolean existIndex(IndexCoordinates indexCoords);

    boolean createIndex(); // 수정 필요

    boolean deleteIndex(IndexCoordinates indexCoords);

}
