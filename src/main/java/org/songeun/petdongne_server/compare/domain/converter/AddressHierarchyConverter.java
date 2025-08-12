package org.songeun.petdongne_server.compare.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressHierarchy;

@Converter
public class AddressHierarchyConverter implements AttributeConverter<AddressHierarchy, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AddressHierarchy attribute) {
        return attribute.getLevel();
    }

    @Override
    public AddressHierarchy convertToEntityAttribute(Integer dbData) {
        return AddressHierarchy.fromLevel(dbData);
    }

}
