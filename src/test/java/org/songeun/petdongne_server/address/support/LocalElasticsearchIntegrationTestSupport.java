package org.songeun.petdongne_server.address.support;

import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.repository.AddressDocumentRepository;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexOperations;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.search.AddressSearchRepository;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;

public class LocalElasticsearchIntegrationTestSupport extends IntegrationTestSupport {

    @Autowired
    protected AddressSearchRepository searchRepository;

    @Autowired
    protected AddressDocumentRepository documentRepository;

    @Autowired
    protected AddressIndexOperations indexOperations;

}
