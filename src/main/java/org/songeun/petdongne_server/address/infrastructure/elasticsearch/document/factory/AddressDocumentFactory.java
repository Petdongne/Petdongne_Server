package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.factory;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressDocumentFactory {

    public AddressDocument create(String code, AddressParts addressParts) {

        return AddressDocument.create(code, addressParts);
    }

}
