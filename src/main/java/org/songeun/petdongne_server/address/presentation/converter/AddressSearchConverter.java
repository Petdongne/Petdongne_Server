package org.songeun.petdongne_server.address.presentation.converter;

import org.songeun.petdongne_server.compare.domain.Address;
import org.songeun.petdongne_server.global.common.PagedResult;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.address.presentation.dto.AddressSearchResponseDto;
import org.songeun.petdongne_server.global.common.SlicedResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public class AddressSearchConverter {

    public static PagedResult<AddressSearchResponseDto> convert(final Page<AddressDocument> addressDocuments) {
        Page<AddressSearchResponseDto> responseDtoPage = addressDocuments.map(
                addressDocument -> AddressSearchResponseDto.of(
                        addressDocument.getSido(),
                        addressDocument.getSigungu(),
                        addressDocument.getEupmyeondong(),
                        addressDocument.getRe(),
                        addressDocument.getFullAddress()
                )
        );

        return PagedResult.from(responseDtoPage);
    }

    public static SlicedResult<AddressSearchResponseDto> convert(final Slice<Address> addressChunk) {
        addressChunk.map(address -> AddressSearchResponseDto.of(

        ))
    }

}
