package org.songeun.petdongne_server.compare.application.dto;

import lombok.Builder;

@Builder
public record AddressSearchRequestDto(
        String searchText,
        int page,
        int size
) {
    public static AddressSearchRequestDto of(final String searchText, final int page, final int size) {
        return AddressSearchRequestDto.builder()
                .searchText(searchText)
                .page(page)
                .size(size).build();
    }
}
