package org.songeun.petdongne_server.address.presentation.converter;

import org.songeun.petdongne_server.global.common.PagedResult;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.address.presentation.dto.AddressSearchResponseDto;
import org.springframework.data.domain.Page;

public class AddressSearchConverter {

    public static PagedResult<AddressSearchResponseDto> convert(final Page<AddressDocument> addressDocuments) {
        Page<AddressSearchResponseDto> responseDtoPage = addressDocuments.map(
                addressDocument -> AddressSearchResponseDto.of(
                        addressDocument.getSido(),
                        addressDocument.getSigungu(),
                        addressDocument.getEupmyeondong(),
                        addressDocument.getRe(),
                        addressDocument.getFullAddress(),
                        addressDocument.getHierarchyLevel().getLevel()
                )
        );

        return PagedResult.from(responseDtoPage);
    }

}
