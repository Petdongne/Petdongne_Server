package org.songeun.petdongne_server.address.application.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.songeun.petdongne_server.address.application.dto.AddressBoundsSearchResponseDto;
import org.songeun.petdongne_server.address.application.dto.AddressDtoMapper;
import org.songeun.petdongne_server.address.application.dto.AddressSearchRequestDto;
import org.songeun.petdongne_server.address.domain.AddressErrorStatus;
import org.songeun.petdongne_server.address.domain.RegionAddressLevel;
import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressBoundsSearchQueryResponseDto;
import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressSearchQueryResponseDto;
import org.songeun.petdongne_server.global.common.*;
import org.songeun.petdongne_server.address.infrastructure.repository.LegalAddressSearchRepository;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.global.search.*;
import org.songeun.petdongne_server.map.domain.ZoomLevel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressSearchService {

    private final LegalAddressSearchRepository searchRepository;
    private final SynonymResolver regionSynonymResolver;
    private final WhiteSpaceTokenizer whiteSpaceTokenizer;
    private final ZoomLevel zoomLevel;

    // todo: request dto 풀어주기
    public Slice<LegalAddressSearchQueryResponseDto> searchByText(AddressSearchRequestDto requestDto) {
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
