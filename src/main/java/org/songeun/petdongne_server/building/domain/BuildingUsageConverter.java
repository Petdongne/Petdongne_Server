package org.songeun.petdongne_server.building.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BuildingUsageConverter implements AttributeConverter<BuildingUsage, String> {

    @Override
    public String convertToDatabaseColumn(BuildingUsage buildingUsage) {
        return buildingUsage.getCode();
    }

    @Override
    public BuildingUsage convertToEntityAttribute(String s) {
        return BuildingUsage.fromCode(s);
    }

}
