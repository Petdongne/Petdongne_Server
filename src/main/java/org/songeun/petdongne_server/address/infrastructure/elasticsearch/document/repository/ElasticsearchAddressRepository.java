package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.repository;

import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticsearchAddressRepository extends ElasticsearchRepository<AddressDocument, String> {

}
