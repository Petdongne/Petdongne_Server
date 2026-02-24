package org.songeun.petdongne_server.review.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResidenceReviewTest {

    @Test
    @DisplayName("동일 연산 고고")
    void test() {
        //given
        Float d1 = 1.1f;
        Float d2 = 1.1f;

        //when

        //then
        Assertions.assertEquals(d1, d2);
    }

}