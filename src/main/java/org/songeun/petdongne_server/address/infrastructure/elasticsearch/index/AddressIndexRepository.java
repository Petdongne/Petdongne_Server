package org.songeun.petdongne_server.address.infrastructure.elasticsearch.index;

import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.Set;

public interface AddressIndexRepository {

    boolean createIndex(IndexCoordinates indexCoordinates);

    boolean deleteIndex(IndexCoordinates indexCoords);

    boolean setAlias(IndexCoordinates targetCoords);

    Set<String> getIndexNamesByAlias();

    boolean existIndexByAlias();

    boolean existIndex(IndexCoordinates indexCoords);

}
