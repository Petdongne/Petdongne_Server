package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.factory;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressDocumentFactory {

    private final AddressDocumentIdGenerator idGenerator;

    public AddressDocument create(String code, AddressParts addressParts) {
        String fullAddress = addressParts.concatenateParts(" ");
        String id = idGenerator.generate(fullAddress);
        AddressHierarchy hierarchy = AddressHierarchy.determine(addressParts);

        return AddressDocument.create(id, code, addressParts, fullAddress, hierarchy, AddressType.determine(addressParts));
    }

}
