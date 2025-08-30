package org.songeun.petdongne_server.compare.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.songeun.petdongne_server.compare.domain.entity.AddressType;

@Converter
public class AddressTypeConverter implements AttributeConverter<AddressType, String> {

    @Override
    public String convertToDatabaseColumn(AddressType attribute) {
        return attribute.getKoreanName();
    }

    @Override
    public AddressType convertToEntityAttribute(String dbData) {
        return AddressType.fromName(dbData);
    }

}
