package org.songeun.petdongne_server.address.infrastructure.elasticsearch.search;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument.FieldConstants;

@Repository
@Transactional
@RequiredArgsConstructor
public class AddressSearchRepositoryImpl implements AddressSearchRepository {

    public static final int KOR_ADDRESS_MAX_LENGTH = 25; // 현재 주소 최대 길이(21자) + 향후 확장 여유분
    private static final String SPECIAL_QUERY_GWANGJU = "광주광역시";
    private static final String GWANGJU_CITY = "광주시";

    private final SearchOperations operations;

    @Override
    public SearchPage<AddressDocument> searchAddress(String query, Pageable pageable, Sort sort) {
        Query customSearchQuery = buildQueryBasedOnInput(query);
        NativeQuery nativeQuery = buildNativeQuery(customSearchQuery, sort, pageable);

        SearchHits<AddressDocument> search = operations.search(nativeQuery, AddressDocument.class);

        return convertPageFormat(search, pageable);
    }

    private Query buildQueryBasedOnInput(String query) {
        if (isSingleCharQuery(query)) {
            return buildSingleCharFullAddressQuery(query);
        }

        if (isSpecialGwangjuQuery(query)) {
            return buildFullAddressQueryExcluding(query, FieldConstants.SIGUNGU, GWANGJU_CITY);
        }

        return buildDefaultFullAddressQuery(query);
    }

    private Query buildSingleCharFullAddressQuery(String query) {
        return QueryBuilders.match(builder -> builder
                .field(FieldConstants.getFullAddressFirstChar())
                .query(query));
    }

    /**
     * 기본 match + matchPhrase 쿼리를 생성합니다.
     */
    private BoolQuery.Builder buildBaseAddressQuery(String query) {
        Query matchQuery = QueryBuilders.match(builder -> builder
                .field(FieldConstants.FULL_ADDRESS)
                .operator(Operator.And)
                .query(query));

        Query matchPhraseQuery = QueryBuilders.matchPhrase(builder -> builder
                .field(FieldConstants.getFullAddressPerChar())
                .query(query)
                .slop(KOR_ADDRESS_MAX_LENGTH));

        return QueryBuilders.bool().must(matchQuery, matchPhraseQuery);
    }

    /**
     * 특정 조건의 Document를 검색 결과에서 제외하는 mustNot 쿼리를 추가합니다.
     * @param query 검색 쿼리
     * @param excludeField 제외를 원하는 필드 이름
     * @param excludeValue 제외를 원하는 필드값
     * @return 주소 검색 쿼리
     */
    private Query buildFullAddressQueryExcluding(String query, String excludeField, String excludeValue) {
        Query termQuery = QueryBuilders.term(builder -> builder
                .field(excludeField)
                .value(excludeValue));

        BoolQuery boolQuery = buildBaseAddressQuery(query)
                .mustNot(termQuery)
                .build();

        return boolQuery._toQuery();
    }

    private Query buildDefaultFullAddressQuery(String query) {
        return buildBaseAddressQuery(query).build()._toQuery();
    }

    /**
     * 쿼리에 '광주광역시'가 포함되어있는지 확인합니다.
     * character filter에서 '광주광역시'를 '광주시'로 처리함에 따라, '광주광역시' 검색 시 '광주시' 결과가 포함되지 않도록 특별 처리합니다.
     * @param query 검색 쿼리
     * @return '광주광역시'가 포함되어 있으면 true, 아니라면 false를 반환합니다.
     */
    private boolean isSpecialGwangjuQuery(String query) {
        return query.contains(SPECIAL_QUERY_GWANGJU);
    }

    /**
     * 쿼리가 한 글자인지 확인합니다.
     * 기본 검색 쿼리의 must 조건 조합으로는 한 글자 검색이 불가능하므로 별도 처리합니다.
     * @param query 검색 쿼리
     * @return 한 글자이면 true, 아니라면 false를 반환합니다.
     */
    private boolean isSingleCharQuery(String query) {
        return query != null && query.length() == 1;
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
