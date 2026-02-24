package org.songeun.petdongne_server.review.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.building.infrastructure.repository.BuildingRepository;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.common.Image;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.global.exception.SystemException;
import org.songeun.petdongne_server.review.domain.ResidenceReview;
import org.songeun.petdongne_server.review.domain.ReviewPhoto;
import org.songeun.petdongne_server.review.infrastructure.ResidenceReviewRepository;
import org.songeun.petdongne_server.review.infrastructure.ReviewPhotoRepository;
import org.songeun.petdongne_server.survey.infrastructure.S3Uploader;
import org.songeun.petdongne_server.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class ResidenceReviewService {

    private final ResidenceReviewRepository reviewRepository;
    private final BuildingRepository buildingRepository;
    private final S3Uploader s3Uploader;
    private final ReviewPhotoRepository photoRepository;

    @Transactional
    public CreateReviewResponseDto createReview(@Valid CreateReviewRequestDto request) {
        User user = request.principal().getUser();
        Building building = buildingRepository.findById(request.buildingId())
                .orElseThrow(() -> new BusinessException(GlobalErrorStatus.BAD_REQUEST));

        if (isAlreadyReviewed(user, building)) {
            throw new BusinessException(GlobalErrorStatus.BAD_REQUEST, "리뷰 작성 이력이 있어 새로운 리뷰를 작성할 수 없습니다");
        }
        List<Image> reviewImages = request.images();
        if (reviewImages != null || !reviewImages.isEmpty()) {
            ResidenceReview.validateImgCount(reviewImages.size());
        }
        ResidenceReview.validateInvalidSurveyAnswers(request.answers());

        ResidenceReview review = ResidenceReview.create(
                request.rating(),
                request.content(),
                request.residenceYear(),
                building,
                user
        );
        ResidenceReview saved = reviewRepository.save(review);
        if (reviewImages != null || !reviewImages.isEmpty()) {
            List<ReviewPhoto> uploaded = uploadImagesOrThrow(reviewImages, review);
            photoRepository.saveAll(uploaded);
        }
        return new CreateReviewResponseDto(saved.getId());
    }

    private boolean isAlreadyReviewed(User user, Building building) {
        if (reviewRepository.existWith(user, building)) {
            log.warn("Residence review creation denied -" +
                    " User already reviewed this building. User: {}, Building: {}", user.getId(), building.getId());
            return true;
        }

        return false;
    }

    private List<ReviewPhoto> uploadImagesOrThrow(List<Image> images, ResidenceReview review) {
        List<CompletableFuture<String>> futures = images.stream()
                .map(image -> {
                    try {
                        return s3Uploader.uploadAsync(image.getFile(), "reviews/" + review.getId());
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .toList();

        List<ReviewPhoto> reviewPhotos;

        try {
            reviewPhotos = CompletableFuture
                    .allOf(futures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> futures.stream()
                            .map(CompletableFuture::join)
                            .map(url -> ReviewPhoto.of(url, review))
                            .toList())
                    .join();
        } catch (CompletionException e) {
            log.info("리뷰 이미지 업로드 중 문제가 발생했습니다. ", e);
            throw new SystemException(GlobalErrorStatus.INTERNAL_SERVER_ERROR);
        }
        return reviewPhotos;
    }
}
