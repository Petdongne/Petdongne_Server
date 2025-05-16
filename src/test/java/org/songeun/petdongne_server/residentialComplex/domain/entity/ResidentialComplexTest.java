package org.songeun.petdongne_server.residentialComplex.domain.entity;

import org.assertj.core.api.Assertions;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.testSupport.GeometryTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus.*;

class ResidentialComplexTest {

    @Test
    @DisplayName("거주 단지 이름, 유형, 좌표, 사용 승인일로 ResidentialComplex 인스턴스를 생성한다.")
    void createResidentialComplex(){
        //given
        String name = "왈왈빌라";
        ResidentialComplexType type = ResidentialComplexType.APARTMENT;
        Point<G2D> location = GeometryTestUtils.defaultPoint();
        LocalDate approvalDate = LocalDate.of(2020, 9, 10);

        //when
        ResidentialComplex complex = ResidentialComplex.of(name, type, location, approvalDate);

        //then
        Assertions.assertThat(complex)
                .extracting("name", "type", "location", "approvalDate")
                .containsExactly(name, type, location, approvalDate);

    }

    @Test
    @DisplayName("거주 단지 이름이 null이면 예외를 발생시킨다.")
    void fail_createResidentialComplex_whenNameIsNull(){
        //given
        String invalidName = null;
        ResidentialComplexType type = ResidentialComplexType.APARTMENT;
        Point<G2D> location = GeometryTestUtils.defaultPoint();
        LocalDate approvalDate = LocalDate.of(2020, 9, 10);

        //when //then
        assertThatThrownBy(() -> ResidentialComplex.of(invalidName, type, location, approvalDate))
                .isInstanceOf(BusinessException.class)
                .hasMessage(RESIDENTIAL_COMPLEX_NAME_REQUIRED.getMessage());

    }

    @Test
    @DisplayName("거주 단지 유형이 null이면 예외를 발생시킨다.")
    void fail_createResidentialComplex_whenTypeIsNull(){
        //given
        String name = "왈왈빌라";
        ResidentialComplexType invalidType = null;
        Point<G2D> location = GeometryTestUtils.defaultPoint();
        LocalDate approvalDate = LocalDate.of(2020, 9, 10);

        //when //then
        assertThatThrownBy(() -> ResidentialComplex.of(name, invalidType, location, approvalDate))
                .isInstanceOf(BusinessException.class)
                .hasMessage(RESIDENTIAL_COMPLEX_TYPE_REQUIRED.getMessage());

    }

    @Test
    @DisplayName("거주 단지 위치가 null이면 예외를 발생시킨다.")
    void fail_createResidentialComplex_whenLocationIsNull(){
        //given
        String name = "왈왈빌라";
        ResidentialComplexType type = ResidentialComplexType.APARTMENT;
        Point<G2D> invalidLocation = null;
        LocalDate approvalDate = LocalDate.of(2020, 9, 10);

        //when //then
        assertThatThrownBy(() -> ResidentialComplex.of(name, type, invalidLocation, approvalDate))
                .isInstanceOf(BusinessException.class)
                .hasMessage(RESIDENTIAL_COMPLEX_LOCATION_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("거주 단지 사용 승인일이 null이면 예외를 발생시킨다.")
    void fail_createResidentialComplex_whenApprovalDateIsNull(){
        //given
        String name = "왈왈빌라";
        ResidentialComplexType type = ResidentialComplexType.APARTMENT;
        Point<G2D> location = GeometryTestUtils.defaultPoint();
        LocalDate invalidApprovalDate = null;

        //when //then
        assertThatThrownBy(() -> ResidentialComplex.of(name, type, location, invalidApprovalDate))
                .isInstanceOf(BusinessException.class)
                .hasMessage(APPROVAL_DATE_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("거주 단지 이름이 공백이면 예외를 발생시킨다.")
    void fail_createResidentialComplex_whenNameIsInvalid(){
        //given
        String invalidName = "  ";
        ResidentialComplexType type = ResidentialComplexType.APARTMENT;
        Point<G2D> location = GeometryTestUtils.defaultPoint();
        LocalDate date = LocalDate.of(2020, 9, 10);

        //when //then
        assertThatThrownBy(() -> ResidentialComplex.of(invalidName, type, location, date))
                .isInstanceOf(BusinessException.class)
                .hasMessage(RESIDENTIAL_COMPLEX_NAME_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("거주 단지 사용 승인일이 미래이면 예외를 발생시킨다.")
    void fail_createResidentialComplex_whenDateIsFuture(){
        //given
        String invalidName = "왈왈빌라";
        ResidentialComplexType type = ResidentialComplexType.APARTMENT;
        Point<G2D> location = GeometryTestUtils.defaultPoint();
        LocalDate date = LocalDate.of(2300, 9, 10);

        //when //then
        assertThatThrownBy(() -> ResidentialComplex.of(invalidName, type, location, date))
                .isInstanceOf(BusinessException.class)
                .hasMessage(APPROVAL_DATE_IS_FUTURE.getMessage());
    }

}