package org.songeun.petdongne_server.address.infrastructure.elasticsearch.index;

public interface AddressIndexRepository {

    public boolean existIndex();

    public boolean createIndex();

}
