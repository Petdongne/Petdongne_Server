package org.songeun.petdongne_server.search.infrastructure.converter;

import org.songeun.petdongne_server.search.domain.document.AddressType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class AddressTypeToStringConverter implements Converter<AddressType, String> {

    @Override
    public String convert(AddressType type) {
        return type.getKoreanName();

    }

}