package org.songeun.petdongne_server.residentialComplex.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.testSupport.GeometryTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class AreaTest {

    @Test
    @DisplayName("주거단지, 면적으로 Area 인스턴스를 생성한다.")
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

    private ResidentialComplex createResidentialComplex() {
        return ResidentialComplex.of(
                "왈왈아파트",
                ResidentialComplexType.APARTMENT,
                GeometryTestUtils.defaultPoint(),
                LocalDate.of(2020, 9, 10)
        );
    }

}