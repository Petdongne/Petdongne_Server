package org.songeun.petdongne_server.address.application;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.songeun.petdongne_server.address.domain.AddressErrorStatus;
import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressSearchQueryResponseDto;
import org.songeun.petdongne_server.global.common.*;
import org.songeun.petdongne_server.address.infrastructure.repository.LegalAddressSearchRepository;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.global.search.KorSearchTextNormalizer;
import org.songeun.petdongne_server.global.search.OrderedTokens;
import org.songeun.petdongne_server.global.search.SynonymResolver;
import org.songeun.petdongne_server.global.search.WhiteSpaceTokenizer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressSearchService {

    private final LegalAddressSearchRepository searchRepository;
    private final SynonymResolver regionSynonymResolver;
    private final WhiteSpaceTokenizer whiteSpaceTokenizer;

    public Slice<LegalAddressSearchQueryResponseDto> search(AddressSearchRequestDto requestDto) {
        if (requestDto == null) {
            throw new BusinessException(GlobalErrorStatus.NULL_OBJECT_NOT_ALLOWED);
        }

        if (StringUtils.isBlank(requestDto.searchText())) {
            throw new BusinessException(AddressErrorStatus.SEARCH_TEXT_NULL_OR_EMPTY_NOT_ALLOWED);
        }

        String plainText = requestDto.searchText();
        String normalized = KorSearchTextNormalizer.normalize(plainText);
        OrderedTokens tokens = whiteSpaceTokenizer.tokenize(normalized);
        tokens.resolveSynonym(regionSynonymResolver);

        if (tokens.hasSingleAndOneLenToken()) {
            return searchRepository.searchAddressInitials(
                    tokens.toToken(),
                    PageRequest.of(requestDto.page(), requestDto.size()));
        } else {
            return searchRepository.searchFullAddress(
                    tokens,
                    PageRequest.of(requestDto.page(), requestDto.size())
            );
        }
    }

}
