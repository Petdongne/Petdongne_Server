package org.songeun.petdongne_server.addess.infrastructure.elasticsearch.index;

public interface AddressIndexRepository {

    public boolean existIndex();

    public boolean createIndex();

}
