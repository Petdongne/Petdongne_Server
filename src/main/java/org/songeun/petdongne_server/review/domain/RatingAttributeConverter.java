package org.songeun.petdongne_server.review.domain;

import jakarta.persistence.AttributeConverter;

public class RatingAttributeConverter implements AttributeConverter<Rating, Double> {

    @Override
    public Double convertToDatabaseColumn(Rating rating) {
        return rating.getDoubleValue();
    }

    @Override
    public Rating convertToEntityAttribute(Double value) {
        return Rating.fromValue(value);
    }
}
