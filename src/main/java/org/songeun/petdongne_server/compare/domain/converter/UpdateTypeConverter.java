package org.songeun.petdongne_server.compare.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.songeun.petdongne_server.compare.domain.entity.UpdateType;

@Converter
public class UpdateTypeConverter implements AttributeConverter<UpdateType, String> {

    @Override
    public String convertToDatabaseColumn(UpdateType attribute) {
        return attribute.toDbData();
    }

    @Override
    public UpdateType convertToEntityAttribute(String dbData) {
        return UpdateType.fromDbData(dbData);
    }

}
