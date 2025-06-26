package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.converter;

import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressHierarchy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class AddressHierarchyToIntegerConverter implements Converter<AddressHierarchy, Integer> {

    @Override
    public Integer convert(AddressHierarchy hierarchy) {
        return hierarchy.getLevel();
    }

}
