package org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.converter;

import org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.AddressHierarchy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class IntegerToAddressHierarchyConverter implements Converter<Integer, AddressHierarchy> {

    @Override
    public AddressHierarchy convert(Integer level) {
        return AddressHierarchy.fromLevel(level);
    }

}
