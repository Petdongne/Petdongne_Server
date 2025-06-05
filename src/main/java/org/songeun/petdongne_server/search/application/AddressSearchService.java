package org.songeun.petdongne_server.search.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.search.infrastructure.LegalDongAddressRepository;
import org.songeun.petdongne_server.search.domain.LegalDongAddress;
import org.songeun.petdongne_server.search.presentation.AddressSearchResponseDto;
import org.songeun.petdongne_server.global.common.PagedResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class AddressSearchService {

    private final LegalDongAddressRepository addressRepository;

    public PagedResult<AddressSearchResponseDto> searchFor(String searchText, Pageable pageable) {
        Page<LegalDongAddress> pagedAddresses = addressRepository.searchLegalDongAddressByFullAddress(searchText, pageable);
        Page<AddressSearchResponseDto> mapped = pagedAddresses.map(content -> AddressSearchResponseDto.of(content.getFullAddress()));

        return PagedResult.<AddressSearchResponseDto>builder()
                .currentPage(mapped.getNumber())
                .totalPages(mapped.getTotalPages())
                .hasNext(mapped.hasNext())
                .totalItems(mapped.getSize())
                .hasPrevious(mapped.hasPrevious())
                .items(mapped.getContent())
                .build();
    }

}

