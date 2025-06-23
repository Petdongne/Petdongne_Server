package org.songeun.petdongne_server.addess.infrastructure.elasticsearch.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.AddressDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.AddressDocument.AddressDocumentFields;

@Repository
@Transactional
@RequiredArgsConstructor
public class AddressSearchRepositoryImpl implements AddressSearchRepository {

    private static final String SPECIAL_QUERY_GWANGJU = "광주광역시";
    private static final String GWANGJU_CITY = "광주시";

    private final SearchOperations operations;

    @Override
    public SearchPage<AddressDocument> searchAddress(String query, Pageable pageable, Sort sort) {
        final Query searchQuery;

        if (isSpecialGwangjuQuery(query)) {
            searchQuery = buildFullAddressQueryExcluding(query, AddressDocumentFields.SIGUNGU, GWANGJU_CITY);
        } else if (isSingleCharQuery(query)) {
            searchQuery = buildFirstCharFullAddressQuery(query);
        } else {
            searchQuery = buildDefaultFullAddressQuery(query);
        }

        NativeQuery nativeQuery = buildNativeQuery(searchQuery, sort, pageable);
        SearchHits<AddressDocument> search = operations.search(nativeQuery, AddressDocument.class);

        return convertPageFormat(search, pageable);
    }

    private boolean isSpecialGwangjuQuery(String query) {
        return SPECIAL_QUERY_GWANGJU.equals(query);
    }

    private boolean isSingleCharQuery(String query) {
        return query != null && query.length() == 1;
    }

    private Query buildFullAddressQueryExcluding(String query, String excludeField, String excludeValue) {
        int slop = 50;

        Query matchQuery = QueryBuilders.match(builder -> builder
                .field(AddressDocumentFields.FULL_ADDRESS)
                .operator(Operator.And)
                .query(query));

        Query matchPhraseQuery = QueryBuilders.matchPhrase(builder -> builder
                .field(AddressDocumentFields.getFullAddressPerChar())
                .slop(slop)
                .query(query));

        Query termQuery = QueryBuilders.term(builder -> builder
                .field(excludeField)
                .value(excludeValue));

        return QueryBuilders.bool(builder -> builder
                .must(matchQuery, matchPhraseQuery)
                .mustNot(termQuery));
    }

    private Query buildFirstCharFullAddressQuery(String query) {
        return QueryBuilders.match(builder -> builder
                .field(AddressDocumentFields.getFullAddressFirstChar())
                .query(query));
    }

    private Query buildDefaultFullAddressQuery(String query) {
        int slop = 50;

        Query matchQuery = QueryBuilders.match(builder -> builder
                .field(AddressDocumentFields.FULL_ADDRESS)
                .operator(Operator.And)
                .query(query));

        Query matchPhraseQuery = QueryBuilders.matchPhrase(builder -> builder
                .field(AddressDocumentFields.getFullAddressPerChar())
                .query(query)
                .slop(slop));

        return QueryBuilders.bool(builder -> builder
                .must(matchQuery, matchPhraseQuery));
    }

    private NativeQuery buildNativeQuery(Query searchQuery, Sort searchSort, Pageable pageable) {

        return NativeQuery.builder()
                .withQuery(searchQuery)
                .withSort(searchSort)
                .withPageable(pageable)
                .build();
    }

    private SearchPage<AddressDocument> convertPageFormat(SearchHits<AddressDocument> searched, Pageable pageable) {
        return SearchHitSupport.searchPageFor(searched, pageable);
    }

}
