package org.songeun.petdongne_server.global.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.songeun.petdongne_server.residentialComplex.entity.ResidentialComplexType;

@Converter
public class ResidentialComplexTypeConverter implements AttributeConverter<ResidentialComplexType, String> {

    @Override
    public String convertToDatabaseColumn(ResidentialComplexType type) {
        return type.getCode();
    }

    @Override
    public ResidentialComplexType convertToEntityAttribute(String code) {
        return ResidentialComplexType.fromCode(code);
    }

}
