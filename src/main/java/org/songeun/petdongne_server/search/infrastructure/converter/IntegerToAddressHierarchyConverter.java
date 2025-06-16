package org.songeun.petdongne_server.search.infrastructure.converter;

import org.songeun.petdongne_server.search.domain.document.AddressHierarchy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class IntegerToAddressHierarchyConverter implements Converter<Integer, AddressHierarchy> {

    @Override
    public AddressHierarchy convert(Integer level) {
        return AddressHierarchy.fromLevel(level);
    }

}
