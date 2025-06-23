package org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.converter;

import org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.AddressType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class AddressTypeToStringConverter implements Converter<AddressType, String> {

    @Override
    public String convert(AddressType type) {
        return type.getKoreanName();

    }

}