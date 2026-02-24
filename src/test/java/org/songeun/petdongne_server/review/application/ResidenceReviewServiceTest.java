package org.songeun.petdongne_server.review.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.building.fixture.BuildingFixtureFactory;
import org.songeun.petdongne_server.building.infrastructure.repository.BuildingRepository;
import org.songeun.petdongne_server.global.common.Image;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.global.exception.SystemException;
import org.songeun.petdongne_server.review.domain.Rating;
import org.songeun.petdongne_server.review.domain.ResidenceReview;
import org.songeun.petdongne_server.review.domain.ReviewPhoto;
import org.songeun.petdongne_server.review.infrastructure.ResidenceReviewRepository;
import org.songeun.petdongne_server.review.infrastructure.ReviewPhotoRepository;
import org.songeun.petdongne_server.security.authentication.UserPrincipal;
import org.songeun.petdongne_server.survey.domain.AnswerOption;
import org.songeun.petdongne_server.survey.domain.QuestionType;
import org.songeun.petdongne_server.survey.infrastructure.S3Uploader;
import org.songeun.petdongne_server.testSupport.AuthTestFixture;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class ResidenceReviewServiceTest extends IntegrationTestSupport {

    @Autowired
    private ResidenceReviewService reviewService;

    @Autowired
    private ResidenceReviewRepository residenceReviewRepository;

    @Autowired
    private ReviewPhotoRepository reviewPhotoRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private AuthTestFixture authTestFixture;

    @Autowired
    private BuildingFixtureFactory buildingFixtureFactory;

    @MockitoBean
    private S3Uploader s3Uploader;

    private UserPrincipal userPrincipal;

    @PostConstruct
    void init() {
        userPrincipal = new UserPrincipal(authTestFixture.getUser());
    }

    @AfterEach
    void tearDown() {
        reviewPhotoRepository.deleteAll();
        residenceReviewRepository.deleteAll();
        buildingRepository.deleteAll();
    }

    @Test
    @DisplayName("리뷰를 생성한다")
    void shouldCreateReviewSuccessfully() throws IOException {
        //given
        Building building = buildingFixtureFactory.makeAndSaveBuilding();

        CreateReviewRequestDto requestDto = new CreateReviewRequestDto(
                userPrincipal,
                building.getId(),
                2022,
                Rating.THREE,
                "좋아요! ".repeat(10),
                List.of(
                        new SurveyAnswerDto(QuestionType.NEIGHBOR_REACTION, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.WALKING_ENV, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.SOUND_PROOF, AnswerOption.AVERAGE)
                ),
                List.of()
        );

        //when
        CreateReviewResponseDto responseDto = reviewService.createReview(requestDto);

        //then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.reviewId()).isNotNull();
        ResidenceReview residenceReview = residenceReviewRepository.findById(responseDto.reviewId()).orElseThrow();
        assertThat(residenceReview.getReviewText()).isEqualTo(requestDto.content());
        assertThat(residenceReview.getBuilding().getId()).isEqualTo(requestDto.buildingId());
        assertThat(residenceReview.getRating()).isEqualTo(requestDto.rating());
        assertThat(residenceReview.getLastOccupiedYear()).isEqualTo(requestDto.residenceYear());
    }

    @Test
    @DisplayName("null이면 안돼용")
    void nong() {
        // given
        CreateReviewRequestDto requestDto = new CreateReviewRequestDto(
                null,
                2,
                2022,
                Rating.THREE,
                "좋아요! ".repeat(10),
                List.of(
                        new SurveyAnswerDto(QuestionType.NEIGHBOR_REACTION, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.WALKING_ENV, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.SOUND_PROOF, AnswerOption.AVERAGE)
                ),
                List.of()
        );
        // when

        // then
        CreateReviewResponseDto responseDto = reviewService.createReview(requestDto);
    }

    @Test
    @DisplayName("중복된 질문에 답변하면 오류를 던진다")
    void shouldThrowExceptionWhenDuplicatedAnswer() throws IOException {
        //given
        Building building = buildingFixtureFactory.makeAndSaveBuilding();

        CreateReviewRequestDto requestDto = new CreateReviewRequestDto(
                userPrincipal,
                building.getId(),
                2022,
                Rating.THREE,
                "좋아요! ".repeat(10),
                List.of(
                        new SurveyAnswerDto(QuestionType.NEIGHBOR_REACTION, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.WALKING_ENV, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.WALKING_ENV, AnswerOption.AVERAGE)
                ),
                List.of()
        );

        //when & then
        assertThatThrownBy(() -> reviewService.createReview(requestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("응답이 누락되거나 중복된 설문조사가 있습니다.");
    }

    @Test
    @DisplayName("동일 건물에 거주 리뷰를 남긴 적이 있다면 새로운 리뷰를 생성할 수 없다")
    void shouldFailWhenAlreadyReviewed() throws IOException {
        //given
        Building building = buildingFixtureFactory.makeAndSaveBuilding();
        generateReview(userPrincipal, building);

        CreateReviewRequestDto requestDto = new CreateReviewRequestDto(
                userPrincipal,
                building.getId(),
                2022,
                Rating.THREE,
                "좋아요! ".repeat(10),
                List.of(
                        new SurveyAnswerDto(QuestionType.NEIGHBOR_REACTION, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.WALKING_ENV, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.SOUND_PROOF, AnswerOption.AVERAGE)
                ),
                List.of()
        );

        // when & then
        assertThatThrownBy(() -> reviewService.createReview(requestDto))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("이미지를 포함한 리뷰 내용을 정상적으로 업로드한다")
    void shouldCreateReviewWithImages() throws IOException {
        //given
        Building building = buildingFixtureFactory.makeAndSaveBuilding();
        MockMultipartFile mockFile = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        String imageUrl = "http://s3-image-url.com/image.jpg";
        given(s3Uploader.uploadAsync(any(MultipartFile.class), any()))
                .willReturn(CompletableFuture.completedFuture(imageUrl));

        CreateReviewRequestDto requestDto = new CreateReviewRequestDto(
                userPrincipal,
                building.getId(),
                2022,
                Rating.THREE,
                "좋아요! ".repeat(10),
                List.of(
                        new SurveyAnswerDto(QuestionType.NEIGHBOR_REACTION, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.SOUND_PROOF, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.WALKING_ENV, AnswerOption.AVERAGE)
                ),
                List.of(Image.create(mockFile))
        );

        //when
        CreateReviewResponseDto responseDto = reviewService.createReview(requestDto);

        //then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.reviewId()).isNotNull();
        ResidenceReview residenceReview = residenceReviewRepository.findById(responseDto.reviewId()).orElseThrow();
        assertThat(residenceReview.getReviewText()).isEqualTo(requestDto.content());
        assertThat(residenceReview.getBuilding().getId()).isEqualTo(requestDto.buildingId());
        assertThat(residenceReview.getRating()).isEqualTo(requestDto.rating());
        assertThat(residenceReview.getLastOccupiedYear()).isEqualTo(requestDto.residenceYear());

        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.findAllByReview(residenceReview);
        assertThat(reviewPhotos).hasSize(1);
        assertThat(reviewPhotos.getFirst().getUrl()).isEqualTo(imageUrl);
    }

    @Test
    @DisplayName("업로드 가능 이미지 수량을 초과하면 예외를 던진다")
    void shouldThrowExceptionWhenExceedMaxImageCount() throws IOException {
        // given
        Building building = buildingFixtureFactory.makeAndSaveBuilding();
        int maxImageCount = ResidenceReview.getMaxReviewImageCount();
        List<Image> images = IntStream.range(0, maxImageCount + 1)
                .mapToObj(i -> new MockMultipartFile("image" + i, "image" + i + ".jpg", "image/jpeg", ("test image content" + i).getBytes()))
                .map(Image::create)
                .collect(Collectors.toList());

        CreateReviewRequestDto requestDto = new CreateReviewRequestDto(
                userPrincipal,
                building.getId(),
                2022,
                Rating.THREE,
                "좋아요! ".repeat(10),
                List.of(
                        new SurveyAnswerDto(QuestionType.NEIGHBOR_REACTION, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.SOUND_PROOF, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.WALKING_ENV, AnswerOption.AVERAGE)
                ),
                images
        );

        // when & then
        assertThatThrownBy(() -> reviewService.createReview(requestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이미지 파일은 최대 3개까지 업로드할 수 있습니다.");
    }

    @Test
    @DisplayName("이미지 업로드에 실패하면 리뷰 작성을 취소한다")
    void shouldRollbackWhenImageUploadFailed() throws IOException {
        //given
        Building building = buildingFixtureFactory.makeAndSaveBuilding();
        MockMultipartFile mockFile = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        given(s3Uploader.uploadAsync(any(), any()))
                .willReturn(CompletableFuture.failedFuture(new RuntimeException("Image upload failed")));

        CreateReviewRequestDto requestDto = new CreateReviewRequestDto(
                userPrincipal,
                building.getId(),
                2022,
                Rating.THREE,
                "좋아요! ".repeat(10),
                List.of(
                        new SurveyAnswerDto(QuestionType.NEIGHBOR_REACTION, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.SOUND_PROOF, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.WALKING_ENV, AnswerOption.AVERAGE)
                ),
                List.of(Image.create(mockFile))
        );

        //when & then
        assertThatThrownBy(() -> reviewService.createReview(requestDto))
                .isInstanceOf(SystemException.class);

        // Verify that no review was saved
        assertThat(residenceReviewRepository.existWith(userPrincipal.getUser(), building)).isFalse();
    }

    private void generateReview(UserPrincipal userPrincipal, Building building) {
        CreateReviewRequestDto requestDto = new CreateReviewRequestDto(
                userPrincipal,
                building.getId(),
                2022,
                Rating.THREE,
                "좋아요! ".repeat(10),
                List.of(
                        new SurveyAnswerDto(QuestionType.NEIGHBOR_REACTION, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.WALKING_ENV, AnswerOption.AVERAGE),
                        new SurveyAnswerDto(QuestionType.SOUND_PROOF, AnswerOption.AVERAGE)
                ),
                List.of()
        );
        reviewService.createReview(requestDto);
    }
}