package org.songeun.petdongne_server.address.application.dto;

import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressBoundsSearchQueryResponseDto;

import java.util.List;

public class AddressDtoMapper {

    public static List<AddressBoundsSearchResponseDto> toBoundSearchResponseDtos(
            final List<LegalAddressBoundsSearchQueryResponseDto> dtos) {
        return dtos.stream()
                .map(queryDto -> AddressBoundsSearchResponseDto.of(
                        queryDto.getFullAddress(), queryDto.getLongitude(), queryDto.getLatitude(), queryDto.getRegionLevel().getDescription()
                ))
                .toList();
    }

}
