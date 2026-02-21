package org.songeun.petdongne_server.review.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.songeun.petdongne_server.global.exception.BusinessException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class RatingTest {

    @ParameterizedTest
    @MethodSource("ratingProvider")
    @DisplayName("Double 또는 String 값을 치환한다")
    void shouldReturnRatingFromValidValue(Object input, Rating expected) {
        //when
        Rating obtainedRating;
        if (input instanceof Double value) {
            obtainedRating = Rating.fromValue(value);
        } else {
            obtainedRating = Rating.fromValue((String) input);
        }

        //then
        assertThat(obtainedRating).isEqualTo(expected);
    }

    static Stream<Arguments> ratingProvider() {
        return Stream.of(
                Arguments.of(0.5, Rating.HALF),
                Arguments.of(1.0, Rating.ONE),
                Arguments.of(1.5, Rating.ONE_HALF),
                Arguments.of("0.5", Rating.HALF),
                Arguments.of("1.0", Rating.ONE),
                Arguments.of("1.5", Rating.ONE_HALF)
        );
    }

    @Test
    @DisplayName("일치하는 값이 존재하지 않는 경우 예외를 던진다")
    void shouldThrowExceptionFromInvalidValue() {
        //given
        String rating = "0.49";

        //when & then
        assertThatThrownBy(() -> Rating.fromValue(rating))
                .isInstanceOf(BusinessException.class);
    }

}