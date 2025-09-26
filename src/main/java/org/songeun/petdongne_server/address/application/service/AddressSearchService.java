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

    /**
     * 주어진 범위 내 지역 정보를 반환합니다. 줌 레벨에 따라 반환하는 지역의 레벨이 달라집니다.
     * @param minLat 최소 경도
     * @param minLon 최소 위도
     * @param maxLat 최대 경도
     * @param maxLon 최대 위도
     * @param level 카카오 지도 줌 레벨
     * @return 지역(법정동 주소) 정보
     */
    public List<AddressBoundsSearchResponseDto> searchWithinBounds(
             Double minLon,Double minLat, Double maxLon, Double maxLat, Integer level) {
        Assert.notNull(maxLat, "maxLat must not be null");
        Assert.notNull(maxLon, "maxLon must not be null");
        Assert.notNull(minLat, "minLat must not be null");
        Assert.notNull(minLon, "minLon must not be null");

        RegionAddressLevel regionAddressLevel = zoomLevel.toRegionAddressLevel(level);

        List<LegalAddressBoundsSearchQueryResponseDto> addressesInBounds = searchRepository
                .findAddressWithinBounds(minLon, minLat, maxLon, maxLat, regionAddressLevel);

        return AddressDtoMapper.toBoundSearchResponseDtos(addressesInBounds);
    }

}
