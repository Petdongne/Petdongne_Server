package org.songeun.petdongne_server.address.infrastructure.repository;

import org.songeun.petdongne_server.address.domain.RegionAddressLevel;
import org.songeun.petdongne_server.address.infrastructure.cache.LegalAddressCacheKey;
import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressBoundsSearchQueryResponseDto;
import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressGeoHashSearchQueryResponseDto;
import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressSearchQueryResponseDto;
import org.songeun.petdongne_server.global.search.Token;
import org.songeun.petdongne_server.global.search.OrderedTokens;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Set;

public interface LegalAddressSearchRepository {

    /**
     * 토큰으로 시작하는 주소를 검색합니다.
     * ex) token ='강', result = '서울특별시 강남구' <br>
     * 토큰이 두 글자 이상인 경우, 각 글자는 주소 각 부분의 시작 글자와 매칭됩니다.
     * ex) token = '서강', result = '서울특별시 강남구'
     * @param token 검색어로 사용되는 글자(들)의 단위
     * @param pageRequest 요청하는 페이지 번호 및 크기
     * @return 검색된 법정동 주소
     */
    Slice<LegalAddressSearchQueryResponseDto> searchAddressInitials(Token token, PageRequest pageRequest);

    Slice<LegalAddressSearchQueryResponseDto> searchFullAddress(OrderedTokens tokens, PageRequest pageRequest);

    List<LegalAddressBoundsSearchQueryResponseDto> findAddressWithinBounds(
            Double minLon, Double minLat,
            Double maxLon, Double maxLat,
            RegionAddressLevel regionAddressLevel);

    List<LegalAddressGeoHashSearchQueryResponseDto> findByGeoHashAndLevel(
            Set<String> geoHash, RegionAddressLevel regionAddressLevel);

}
