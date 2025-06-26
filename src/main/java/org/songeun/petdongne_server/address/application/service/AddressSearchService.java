package org.songeun.petdongne_server.address.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.search.AddressSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressSearchService {

    private final AddressSearchRepository searchRepository;

    public Page<AddressDocument> search(String searchText, Pageable pageable) {
        SearchPage<AddressDocument> searchHits = searchRepository.searchAddress(searchText, pageable, Sort.by(
                Sort.Order.asc(AddressDocument.FieldConstants.HIERARCHY_LEVEL),
                Sort.Order.desc(AddressDocument.FieldConstants.SCORE)
        ));

        return searchHits.map(SearchHit::getContent);
    }

}