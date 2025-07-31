package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.factory;

import org.songeun.petdongne_server.global.util.HashGenerator;
import org.springframework.stereotype.Component;

@Component
public class HashIdGenerator implements AddressDocumentIdGenerator {

    public String generate(final String source) {
        return HashGenerator.generate(source);
    }

}