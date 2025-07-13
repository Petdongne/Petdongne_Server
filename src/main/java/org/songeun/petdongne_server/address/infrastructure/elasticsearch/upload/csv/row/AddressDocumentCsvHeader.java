package org.songeun.petdongne_server.address.infrastructure.elasticsearch.upload.csv.row;

import lombok.Getter;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.global.file.validation.ComparableCsvHeader;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AddressDocumentCsvHeader implements ComparableCsvHeader {

    protected static final String ID = "코드";
    protected static final String SIDO = "시도명";
    protected static final String SIGUNGU = "시군구명";
    protected static final String EUPMYEONDONG = "읍면동명";
    protected static final String RE = "리명";
    protected static final String FULLADDRESS = "주소";
    protected static final String HIERARCHY_LEVEL = "계층";
    protected static final String TYPE = "유형";

    @Getter
    private static Set<String> headerNames =
            Set.of(ID, SIDO, RE, FULLADDRESS, HIERARCHY_LEVEL, TYPE);

    @Override
    public boolean compare(Set<String> targetHeaders) {
        return targetHeaders.equals(headerNames);
    }

    @Override
    public boolean isSupported(Class<?> clazz) {
        return clazz.equals(AddressDocument.class);
    }

}
