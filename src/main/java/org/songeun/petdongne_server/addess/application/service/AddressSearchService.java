package org.songeun.petdongne_server.addess.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.addess.infrastructure.elasticsearch.search.AddressSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Service;

import static org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.AddressDocument.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressSearchService {

    private final AddressSearchRepository searchRepository;

    public Page<AddressDocument> search(String searchText, Pageable pageable) {
        SearchPage<AddressDocument> searchHits = searchRepository.searchAddress(searchText, pageable, Sort.by(
                Sort.Order.asc(AddressDocumentFields.HIERARCHY_LEVEL),
                Sort.Order.desc(AddressDocumentFields.SCORE)
        ));

        return searchHits.map(SearchHit::getContent);
    }

}