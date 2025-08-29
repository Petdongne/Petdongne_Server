package org.songeun.petdongne_server.compare.presentation;

import org.songeun.petdongne_server.compare.infrastructure.repository.AddressSearchResponse;
import org.songeun.petdongne_server.compare.presentation.dto.AddressSearchResponseDto;
import org.songeun.petdongne_server.global.common.SlicedResult;
import org.springframework.data.domain.Slice;

public class AddressToDtoConverter {

    public static SlicedResult<AddressSearchResponseDto> convert(Slice<AddressSearchResponse> addressSlice) {
        Slice<AddressSearchResponseDto> dtoSlice = addressSlice.map(
                address -> AddressSearchResponseDto.of(address.id(), address.fullAddress()));

        return SlicedResult.from(dtoSlice);
    }

}
