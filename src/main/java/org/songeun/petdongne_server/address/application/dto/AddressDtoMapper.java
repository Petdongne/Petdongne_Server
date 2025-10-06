package org.songeun.petdongne_server.address.application.dto;

import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressGeoHashSearchQueryResponseDto;

import java.util.List;

public class AddressDtoMapper {

    public static AddressBoundsSearchResponseDto toBoundSearchResponseDto(
            final LegalAddressGeoHashSearchQueryResponseDto dto) {
        return AddressBoundsSearchResponseDto.of(
                dto.getName(),
                dto.getLongitude(),
                dto.getLatitude(),
                dto.getRegionAddressLevel().getDescription(),
                dto.getGeoHash()
        );
    }

}
