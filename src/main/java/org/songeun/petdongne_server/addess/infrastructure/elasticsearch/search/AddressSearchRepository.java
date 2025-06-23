package org.songeun.petdongne_server.addess.infrastructure.elasticsearch.search;

import org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.AddressDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchPage;

public interface AddressSearchRepository {

    public SearchPage<AddressDocument> searchAddress(String query, Pageable pageable, Sort sort);

}

