package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.converter;

import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class StringToAddressTypeConverter implements Converter<String, AddressType> {

    @Override
    public AddressType convert(String name) {
        return AddressType.fromName(name);
    }

}