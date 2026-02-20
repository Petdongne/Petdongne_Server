package org.songeun.petdongne_server.review.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.songeun.petdongne_server.review.application.CreateReviewRequestDto;
import org.songeun.petdongne_server.review.application.CreateReviewResponseDto;
import org.songeun.petdongne_server.review.application.ResidenceReviewService;
import org.songeun.petdongne_server.security.authentication.UserPrincipal;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @PostMapping(path = "/{building_id}/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createReview(
            @PathVariable("building_id") Long buildingId,
            @RequestPart("review") @Valid CreateReviewRequestEssentialBodyDto requestDto,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos,
            @AuthenticationPrincipal UserDetails userDetails){
        CreateReviewResponseDto response =
                residenceReviewService.createReview(toDto(userDetails, buildingId, requestDto, photos));
        return ApiResponse.ok(response);
    }

    private CreateReviewRequestDto toDto(
            UserDetails userDetails,
            Long buildingId,
            CreateReviewRequestEssentialBodyDto requestDto,
            List<MultipartFile> photos) {
        return new CreateReviewRequestDto(
                (UserPrincipal) userDetails,
                buildingId,
                requestDto.residenceYear(),
                requestDto.rating(),
                requestDto.content(),
                requestDto.answers(),
                photos
        );
    }

}
