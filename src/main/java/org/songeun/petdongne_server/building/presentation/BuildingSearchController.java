package org.songeun.petdongne_server.building.presentation;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.building.application.BuildingSearchService;
import org.songeun.petdongne_server.building.application.dto.BuildingDetailResponseDto;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/v1/buildings")
@RequiredArgsConstructor
@Validated
public class BuildingSearchController {

    private final BuildingSearchService buildingSearchService;

    @GetMapping("/{building_id}")
    public ResponseEntity<?> getBuildingDetail(@PathVariable("building_id") Long buildingId) {
        return ApiResponse.ok(buildingSearchService.getBuildingDetail(buildingId));
    }
}
