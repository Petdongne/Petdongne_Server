package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.factory;

import org.songeun.petdongne_server.global.util.HashGenerator;
import org.springframework.stereotype.Component;

@Component
public class AddressDocumentHashIdGenerator implements AddressDocumentIdGenerator {

    public String generate(final String source) {
        String cleaned = cleaningData(source);

        return HashGenerator.generate(cleaned);
    }

    private String cleaningData(final String source) {

        // 중복 공백 제거 + 양 끝 공백 제거
        return source.trim().replaceAll("\\s{2,}", " ");
    }

}