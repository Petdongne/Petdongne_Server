package org.songeun.petdongne_server.review.presentation;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.songeun.petdongne_server.review.application.ResidenceReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/buildings")
@RequiredArgsConstructor
public class ResidenceReviewController {

    private final ResidenceReviewService residenceReviewService;

    @GetMapping("/{building_id}/reviews/{review_id}")
    public ResponseEntity<?> getReview(
            @PathVariable("building_id") Long buildingId,
            @PathVariable("review_id") Long reviewId){
        // to do implements...
        return ApiResponse.ok(null);
    }

}
