package org.songeun.petdongne_server.compare.infrastructure.repository;

import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressType;

public record AddressSearchResponse(
        Long id,
        String fullAddress,
        AddressType type,
        String addressInitials
) {
}
