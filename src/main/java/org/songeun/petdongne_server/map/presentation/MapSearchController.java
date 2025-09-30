package org.songeun.petdongne_server.map.presentation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.application.dto.AddressBoundsSearchResponseDto;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.songeun.petdongne_server.map.application.MapSearchService;
import org.songeun.petdongne_server.map.domain.KakaoZoomLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/map")
@RequiredArgsConstructor
@Validated
public class MapSearchController {

    private final MapSearchService searchService;

    @GetMapping("/clusters")
    public ResponseEntity<?> search(
            @RequestParam @Min(value = -180) @Max(180) Double minLon,
            @RequestParam @Min(value = -90) @Max(value = 90) Double minLat,
            @RequestParam @Min(value = -180) @Max(180) Double maxLon,
            @RequestParam @Min(value = -90) @Max(value = 90) Double maxLat,
            @RequestParam @Min(1) @Max(14) Integer level
    ){
        List<AddressBoundsSearchResponseDto> searched = searchService.searchClustersWithinBounds(
                minLon, minLat, maxLon, maxLat, KakaoZoomLevelCategory.from(level));

        return ApiResponse.ok(searched);
    }

    @GetMapping("/details")
    public ResponseEntity<?> searchDetails(
            @RequestParam @Min(value = -180) @Max(180) Double minLon,
            @RequestParam @Min(value = -90) @Max(value = 90) Double minLat,
            @RequestParam @Min(value = -180) @Max(180) Double maxLon,
            @RequestParam @Min(value = -90) @Max(value = 90) Double maxLat,
            @RequestParam @Min(1) @Max(14) Integer level
    ){
        List<BuildingBoundSearchQueryResponseDto> searched = searchService.searchDetailsWithinBounds(
                minLon, minLat, maxLon, maxLat, KakaoZoomTier.from(level));

        return ApiResponse.ok(searched);
    }

}
