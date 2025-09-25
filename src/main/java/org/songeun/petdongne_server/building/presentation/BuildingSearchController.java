package org.songeun.petdongne_server.building.presentation;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.building.application.BuildingSearchService;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingBoundSearchQueryResponseDto;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buildings")
@RequiredArgsConstructor
@Validated
public class BuildingSearchController {

    private final BuildingSearchService buildingSearchService;

    @GetMapping
    public ResponseEntity<?> searchBuildingsWithinBounds(
            @RequestParam @Min(value = -180) @Max(180) Double minLon,
            @RequestParam @Min(value = -90) @Max(value = 90) Double minLat,
            @RequestParam @Min(value = -180) @Max(180) Double maxLon,
            @RequestParam @Min(value = -90) @Max(value = 90) Double maxLat
    ) {
        List<BuildingBoundSearchQueryResponseDto> dtos = buildingSearchService
                .searchWithinBounds(minLon, minLat, maxLon, maxLat);

        return ApiResponse.ok(dtos);
    }

}
