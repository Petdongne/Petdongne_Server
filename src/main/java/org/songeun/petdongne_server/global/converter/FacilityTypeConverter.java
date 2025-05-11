package org.songeun.petdongne_server.global.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.songeun.petdongne_server.facility.domain.entity.FacilityType;

@Converter
public class FacilityTypeConverter implements AttributeConverter<FacilityType, String> {

    @Override
    public String convertToDatabaseColumn(FacilityType type) {
        return type.getCode();
    }

    @Override
    public FacilityType convertToEntityAttribute(String code) {
        return FacilityType.fromCode(code);
    }

}
