package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.factory;

public interface AddressDocumentIdGenerator {

    String generate(final String source);

}
