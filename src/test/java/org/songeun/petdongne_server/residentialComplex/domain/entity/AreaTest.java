package org.songeun.petdongne_server.residentialComplex.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.testSupport.GeometryTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus.*;

class AreaTest {

    @Test
    @DisplayName("소속될 주거 단지, 면적으로 Area 인스턴스를 생성한다.")
    void createArea(){
        //given
        ResidentialComplex complex = createResidentialComplex();
        Double areaInSquare = 164.49;

        //when
        Area area = Area.of(areaInSquare, complex);

        //then
        assertThat(area)
                .extracting("areaInSquareMeters", "residentialComplex")
                .containsExactly(areaInSquare, complex);

    }

    @Test
    @DisplayName("주거 면적 값이 null이면 예외를 반환한다.")
    void fail_createArea_when_areaValueIsNull(){
        //given
        ResidentialComplex complex = createResidentialComplex();
        Double invalidAreaInSquare = null;

        //when //then
        assertThatThrownBy(() -> Area.of(invalidAreaInSquare, complex))
                .isInstanceOf(BusinessException.class)
                .hasMessage(AREA_VALUE_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("주거 면적 값이 0이면 예외를 반환한다.")
    void fail_createArea_when_areaValueIs(){
        //given
        ResidentialComplex complex = createResidentialComplex();
        Double invalidAreaInSquare = 0.0;

        //when //then
        assertThatThrownBy(() -> Area.of(invalidAreaInSquare, complex))
                .isInstanceOf(BusinessException.class)
                .hasMessage(AREA_VALUE_MUST_BE_POSITIVE.getMessage());
    }

    @Test
    @DisplayName("소속될 주거 단지가 null이면 예외를 반환한다.")
    void fail_createArea_when_complexIsNull(){
        //given
        ResidentialComplex invalidComplex = null;
        Double areaInSquare = 164.49;

        //when //then
        assertThatThrownBy(() -> Area.of(areaInSquare, invalidComplex))
                .isInstanceOf(BusinessException.class)
                .hasMessage(RESIDENTIAL_COMPLEX_REQUIRED.getMessage());
    }

    private ResidentialComplex createResidentialComplex() {
        return ResidentialComplex.of(
                "왈왈아파트",
                ResidentialComplexType.APARTMENT,
                GeometryTestUtils.defaultPoint(),
                LocalDate.of(2020, 9, 10)
        );
    }

}