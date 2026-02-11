package org.songeun.petdongne_server.building.application.dto;

import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;
import org.songeun.petdongne_server.building.domain.Building;

public record BuildingDetailResponseDto(
        String name,
        Integer dongCount,
        Integer householdCount,
        Integer topFloorCount,
        String approvalYear,
        String approvalMonth,
        String jibunAddress,
        String roadAddress,
        String buildingType,
        MultiPolygon<G2D> polygon,
        Double longitude,
        Double latitude
) {
    public static BuildingDetailResponseDto from(Building building) {
        String approvalYear = building.getApprovalYear();
        String approvalMonth = building.getApprovalMonth();

        String roadAddress = building.getRoadName() + " " + building.getRoadAddressMainNum();
        if (building.getRoadAddressSubNum() != null && !building.getRoadAddressSubNum().isEmpty()) {
            roadAddress += "-" + building.getRoadAddressSubNum();
        }

        return new BuildingDetailResponseDto(
                building.getName(),
                building.getDongCount(),
                building.getHouseholdCount(),
                building.getTopFloorCount(),
                approvalYear,
                approvalMonth,
                building.getJibunAddress(),
                roadAddress,
                building.getBuildingUsage().name(), // Assuming BuildingUsage enum has a .name() method
                building.getPolygon(),
                building.getLongitude(),
                building.getLatitude()
        );
    }
}
