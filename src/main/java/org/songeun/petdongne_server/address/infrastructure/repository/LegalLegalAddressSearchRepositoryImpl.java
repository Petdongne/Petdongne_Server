package org.songeun.petdongne_server.address.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.songeun.petdongne_server.address.infrastructure.dto.QLegalAddressSearchQueryResponseDto;
import org.songeun.petdongne_server.global.search.Token;
import org.songeun.petdongne_server.global.search.OrderedTokens;
import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressSearchQueryResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.songeun.petdongne_server.address.domain.QLegalAddress.legalAddress;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LegalLegalAddressSearchRepositoryImpl implements LegalAddressSearchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<LegalAddressSearchQueryResponseDto> searchAddressInitials(Token token, PageRequest pageRequest) {
        if (token == null || token.getValue().isEmpty()) {
            return new SliceImpl<>(Collections.emptyList(), pageRequest, false);
        }

        NumberTemplate<Float> similarityScore = Expressions.numberTemplate(
                Float.class, "similarity({0}, {1})", legalAddress.addressInitials, token.getValue());

        List<LegalAddressSearchQueryResponseDto> fetched = queryFactory.select(
                        new QLegalAddressSearchQueryResponseDto(
                                legalAddress.id,
                                legalAddress.fullAddress,
                                Expressions.numberTemplate(
                                        Double.class, "ST_X({0})",
                                        legalAddress.centerPoint),
                                Expressions.numberTemplate(
                                        Double.class, "ST_Y({0})",
                                        legalAddress.centerPoint)
                        ))
                .from(legalAddress)
                .where(legalAddress.addressInitials.contains(token.getValue()))
                .orderBy(similarityScore.desc(), legalAddress.id.asc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize() + 1)
                .fetch();

        boolean hasNextPage = fetched.size() > pageRequest.getPageSize();
        if (hasNextPage) {
            fetched.removeLast();
        }

        return new SliceImpl<>(fetched, pageRequest, hasNextPage);
    }

    @Override
    public Slice<LegalAddressSearchQueryResponseDto> searchFullAddress(OrderedTokens tokens, PageRequest pageRequest) {
        if (tokens == null) {
            return new SliceImpl<>(Collections.emptyList(), pageRequest, false);
        }

        String concatenatedTokens = tokens.concatTokensWithDelimiter(StringUtils.SPACE);
        NumberTemplate<Float> similarityScore = Expressions.numberTemplate(
                Float.class, "similarity({0}, {1})", legalAddress.fullAddress, concatenatedTokens);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (Token token : tokens.getTokens()) {
            booleanBuilder.and(legalAddress.fullAddress.contains(token.getValue()));
        }

        List<LegalAddressSearchQueryResponseDto> fetched = queryFactory.select(
                        new QLegalAddressSearchQueryResponseDto(
                                legalAddress.id,
                                legalAddress.fullAddress,
                                Expressions.numberTemplate(
                                        Double.class, "ST_X({0})",
                                        legalAddress.centerPoint),
                                Expressions.numberTemplate(
                                        Double.class, "ST_Y({0})",
                                        legalAddress.centerPoint)
                        ))
                .from(legalAddress)
                .where(booleanBuilder)
                .orderBy(similarityScore.desc(), legalAddress.id.asc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize() + 1)
                .fetch();

        boolean hasNextPage = fetched.size() > pageRequest.getPageSize();
        if (hasNextPage) {
            fetched.removeLast();
        }

        return new SliceImpl<>(fetched, pageRequest, hasNextPage);
    }

}
