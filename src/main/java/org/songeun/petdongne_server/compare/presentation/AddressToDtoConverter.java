package org.songeun.petdongne_server.compare.presentation;

import org.songeun.petdongne_server.compare.domain.Address;
import org.songeun.petdongne_server.compare.presentation.dto.AddressSearchResponseDto;
import org.songeun.petdongne_server.global.common.SlicedResult;
import org.springframework.data.domain.Slice;

public class AddressToDtoConverter {

    public static SlicedResult<AddressSearchResponseDto> convert(Slice<Address> addressSlice) {
        Slice<AddressSearchResponseDto> dtoSlice = addressSlice.map(
                address -> AddressSearchResponseDto.of(address.getId(), address.getFullAddress()));

        return SlicedResult.from(dtoSlice);
    }

}
