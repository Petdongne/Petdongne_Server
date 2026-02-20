package org.songeun.petdongne_server.review.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.songeun.petdongne_server.review.application.CreateReviewRequestDto;
import org.songeun.petdongne_server.review.application.CreateReviewResponseDto;
import org.songeun.petdongne_server.review.application.ResidenceReviewService;
import org.songeun.petdongne_server.review.application.SurveyAnswerDto;
import org.songeun.petdongne_server.survey.domain.AnswerOption;
import org.songeun.petdongne_server.survey.domain.QuestionType;
import org.songeun.petdongne_server.testSupport.AuthTestFixture;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ResidenceReviewControllerTest extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResidenceReviewService reviewService;

    @Autowired
    private AuthTestFixture testFixture;

    private Cookie sessionCookie;

    @PostConstruct
    void setUp() {
        String sessionId = testFixture.getSessionId();
        sessionCookie = testFixture.getSessionCookie(sessionId);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidRequest")
    @DisplayName("필수 값 누락/공백 시 400 Bad Request 에러를 반환한다")
    void shouldThrowBadRequestErrorWhenOmit(
            String description,
            MockMultipartFile reveiwContent) throws Exception {
        mockMvc.perform(multipart("/api/v1/buildings/1/reviews")
                        .file(reveiwContent)
                        .cookie(sessionCookie)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));

        verify(reviewService, never()).createReview(any(CreateReviewRequestDto.class));
    }

    private static Stream<Arguments> invalidRequest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Arguments noResidenceYear = Arguments.of("거주년도 누락", new MockMultipartFile(
                "review", "review",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(Map.of(
                        "rating", validRating(),
                        "content", validContent(),
                        "answers", validAnswers())
                ))
        );

        Arguments noRating = Arguments.of("별점 누락", new MockMultipartFile(
                "review", "review",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(Map.of(
                        "residenceYear", validResidenceYear(),
                        "content", validContent(),
                        "answers", validAnswers())
                ))
        );

        Arguments noContent = Arguments.of("내용 누락", new MockMultipartFile(
                "review", "review",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(Map.of(
                        "residenceYear", validResidenceYear(),
                        "rating", validRating(),
                        "answers", validAnswers())
                ))
        );

        Arguments blankContent = Arguments.of("내용 공백", new MockMultipartFile(
                "review", "review",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(Map.of(
                        "residenceYear", validResidenceYear(),
                        "rating", validRating(),
                        "content", " ",
                        "answers", validAnswers())
                ))
        );

        Arguments noAnswer = Arguments.of("답변 누락", new MockMultipartFile(
                "review", "review",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(Map.of(
                        "residenceYear", validResidenceYear(),
                        "rating", validRating(),
                        "content", validContent())
                ))
        );

        return Stream.of(noResidenceYear, noRating, noContent, blankContent, noAnswer);
    }

    private static Integer validResidenceYear() {
        return 2022;
    }

    private static double validRating() {
        return 4.0;
    }

    private static String validContent() {
        return "강아지와 함께하기에 최적의 집입니다. 도보 5분거리 24시 동물병원이 존재하고, 산책 환경이 매우 좋습니다. 강아지를 키우는 분들이 많아 비교적 화목한 분위기였어요.";
    }

    private static List<SurveyAnswerDto> validAnswers() {
        return List.of(
                new SurveyAnswerDto(QuestionType.WALKING_ENV, AnswerOption.AVERAGE),
                new SurveyAnswerDto(QuestionType.SOUND_PROOF, AnswerOption.AVERAGE),
                new SurveyAnswerDto(QuestionType.NEIGHBOR_REACTION, AnswerOption.AVERAGE)
        );
    }

    @Test
    @DisplayName("요청 성공 시 리뷰 통계와 함께 200 OK를 반환한다")
    void shouldReturnOkWhenSuccess() throws Exception {
        // given
        CreateReviewResponseDto responseDto = createResponseDto();
        given(reviewService.createReview(any(CreateReviewRequestDto.class))).willReturn(responseDto);

        Map<String, Object> requestBody = Map.of(
                "residenceYear", validResidenceYear(),
                "rating", validRating(),
                "content", validContent(),
                "answers", validAnswers()
        );
        ObjectMapper mapper = new ObjectMapper();
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "review",
                "review",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(requestBody));

        // when & then
        mockMvc.perform(multipart("/api/v1/buildings/1/reviews")
                        .file(mockMultipartFile)
                        .cookie(sessionCookie)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        verify(reviewService, times(1)).createReview(any(CreateReviewRequestDto.class));
    }

    private CreateReviewResponseDto createResponseDto() {
        return new CreateReviewResponseDto(
                1L,
                new CreateReviewResponseDto.BuildingReviewStatsDto(4.3, 42L),
                List.of(
                        new CreateReviewResponseDto.BuildingQuestionStatDto(
                                QuestionType.SOUND_PROOF,
                                Map.of(
                                        AnswerOption.AVERAGE, 0.4,
                                        AnswerOption.GOOD, 0.3,
                                        AnswerOption.BAD, 0.3
                                )
                        ),
                        new CreateReviewResponseDto.BuildingQuestionStatDto(
                                QuestionType.WALKING_ENV,
                                Map.of(
                                        AnswerOption.GOOD, 0.3,
                                        AnswerOption.BAD, 0.2,
                                        AnswerOption.AVERAGE, 0.5
                                )
                        ),
                        new CreateReviewResponseDto.BuildingQuestionStatDto(
                                QuestionType.NEIGHBOR_REACTION,
                                Map.of(
                                        AnswerOption.AVERAGE, 0.6,
                                        AnswerOption.GOOD, 0.25,
                                        AnswerOption.BAD, 0.15
                                )
                        )
                )
        );
    }

}