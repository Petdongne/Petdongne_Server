package org.songeun.petdongne_server.compare.application.service;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.compare.domain.AddressSearchTextProcessor;
import org.songeun.petdongne_server.compare.domain.ProcessedSearchText;
import org.songeun.petdongne_server.compare.infrastructure.repository.AddressRepository;
import org.songeun.petdongne_server.compare.application.dto.AddressSearchRequestDto;
import org.songeun.petdongne_server.compare.infrastructure.repository.AddressSearchResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AddressSearchService {

    private final AddressRepository addressRepository;
    private final AddressSearchTextProcessor searchTextProcessor;

    public Slice<AddressSearchResponse> search(AddressSearchRequestDto requestDto) {
        ProcessedSearchText searchText = searchTextProcessor.process(requestDto.searchText());
        if (searchText.isEmpty()) {
            return emptyResult(requestDto);
        }

        if (searchText.isSingleCharacter()) {
            return addressRepository.searchForAddressInitials(
                    searchText.getContentAsString(),
                    PageRequest.of(requestDto.page(), requestDto.size()));
        }

        return addressRepository.searchForAddress(
                searchText.getContent(),
                PageRequest.of(requestDto.page(), requestDto.size())
        );
    }

    // 여기서 준 페이지 정보가 어디서 쓰이는지 확인.
    private SliceImpl<AddressSearchResponse> emptyResult(AddressSearchRequestDto requestDto) {
        return new SliceImpl<>(Collections.emptyList(), PageRequest.of(requestDto.page(), requestDto.size()), false);
    }

}
