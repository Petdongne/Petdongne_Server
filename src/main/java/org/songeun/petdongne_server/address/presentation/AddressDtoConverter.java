package org.songeun.petdongne_server.address.presentation;

import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressSearchQueryResponseDto;
import org.songeun.petdongne_server.global.common.SliceResult;
import org.springframework.data.domain.Slice;

public class AddressDtoConverter {

    public static SliceResult<AddressSearchResponseDto> convert(Slice<LegalAddressSearchQueryResponseDto> queryResponses) {
        Slice<AddressSearchResponseDto> dto = queryResponses.map(
                address -> AddressSearchResponseDto.of(
                        address.getId(), address.getFullAddress(), address.getCenterPoint()));

        return SliceResult.from(dto);
    }

}
