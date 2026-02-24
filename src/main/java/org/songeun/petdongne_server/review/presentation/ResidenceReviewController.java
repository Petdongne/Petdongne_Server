package org.songeun.petdongne_server.review.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.songeun.petdongne_server.global.common.Image;
import org.songeun.petdongne_server.global.util.ImageFileConverter;
import org.songeun.petdongne_server.review.application.CreateReviewRequestDto;
import org.songeun.petdongne_server.review.application.CreateReviewResponseDto;
import org.songeun.petdongne_server.review.application.ResidenceReviewService;
import org.songeun.petdongne_server.review.domain.Rating;
import org.songeun.petdongne_server.security.authentication.UserPrincipal;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/buildings")
@RequiredArgsConstructor
public class ResidenceReviewController {

    private final ResidenceReviewService residenceReviewService;
    private final ImageFileConverter imageFileConverter;

    @GetMapping("/{building_id}/reviews/{review_id}")
    public ResponseEntity<?> getReview(
            @PathVariable("building_id") Long buildingId,
            @PathVariable("review_id") Long reviewId){
        return ApiResponse.ok(null);
    }

    @PostMapping(path = "/{building_id}/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createReview(
            @PathVariable("building_id") Long buildingId,
            @RequestPart("review") @Valid CreateReviewRequestEssentialBodyDto requestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal UserDetails userDetails){
        CreateReviewResponseDto response =
                residenceReviewService.createReview(toDto(userDetails, buildingId, requestDto, images));
        return ApiResponse.ok(response);
    }

    private CreateReviewRequestDto toDto(
            UserDetails userDetails,
            Long buildingId,
            CreateReviewRequestEssentialBodyDto requestDto,
            List<MultipartFile> images) {
        return new CreateReviewRequestDto(
                (UserPrincipal) userDetails,
                buildingId,
                requestDto.residenceYear(),
                Rating.fromValue(requestDto.rating()),
                requestDto.content(),
                requestDto.answers(),
                images == null ? null
                        : images.stream().map(imageFileConverter::parse).toList()
        );
    }

}
