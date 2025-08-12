package org.songeun.petdongne_server.compare.application.service;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.compare.domain.Address;
import org.songeun.petdongne_server.compare.domain.AddressSearchTextProcessor;
import org.songeun.petdongne_server.compare.domain.ProcessedSearchText;
import org.songeun.petdongne_server.compare.infrastructure.repository.AddressRepository;
import org.songeun.petdongne_server.compare.application.dto.AddressSearchRequestDto;
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

    public Slice<Address> search(AddressSearchRequestDto requestDto) {
        ProcessedSearchText processedSearchText = searchTextProcessor.process(requestDto.searchText());
        if (processedSearchText.isEmpty()) {
            return emptyResult(requestDto);
        }

        PageRequest pageRequest = PageRequest.of(requestDto.page(), requestDto.size());

        if (processedSearchText.isSingleCharacter()) {
            return addressRepository.findByAddressInitialsContaining(
                    processedSearchText.toQueryWhenSingleChar(),
                    pageRequest);
        }

        return addressRepository.findByAddressContainingKeywords(
                processedSearchText.toQueryWhenMulti(),
                pageRequest
        );
    }

    private SliceImpl<Address> emptyResult(AddressSearchRequestDto requestDto) {
        return new SliceImpl<>(Collections.emptyList(), PageRequest.of(requestDto.page(), requestDto.size()), false);
    }

}
