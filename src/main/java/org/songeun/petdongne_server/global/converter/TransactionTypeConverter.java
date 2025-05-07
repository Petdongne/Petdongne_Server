package org.songeun.petdongne_server.global.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.songeun.petdongne_server.residentialComplex.domain.TransactionType;

@Converter
public class TransactionTypeConverter implements AttributeConverter<TransactionType, String> {

    @Override
    public String convertToDatabaseColumn(TransactionType type) {
        return type.getCode();
    }

    @Override
    public TransactionType convertToEntityAttribute(String code) {
        return TransactionType.fromCode(code);
    }

}
