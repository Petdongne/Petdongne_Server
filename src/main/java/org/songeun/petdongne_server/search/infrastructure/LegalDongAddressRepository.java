package org.songeun.petdongne_server.search.infrastructure;

import org.songeun.petdongne_server.search.domain.LegalDongAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegalDongAddressRepository extends ElasticsearchRepository<LegalDongAddress, String> {

    Page<LegalDongAddress> searchLegalDongAddressByFullAddress(String fullAddress, Pageable pageable);

}
