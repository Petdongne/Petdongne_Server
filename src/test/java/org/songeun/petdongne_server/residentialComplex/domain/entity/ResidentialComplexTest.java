package org.songeun.petdongne_server.residentialComplex.domain.entity;

import org.assertj.core.api.Assertions;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.testSupport.GeometryTestUtils;

import java.time.LocalDate;

class ResidentialComplexTest {

    @Test
    @DisplayName("시설 이름, 유형, 좌표, 사용 승인일로 ResidentialComplex 인스턴스를 생성한다.")
    void createResidentialComplex(){
        //given
        String name = "왈왈빌라";
        ResidentialComplexType type = ResidentialComplexType.APARTMENT;
        Point<G2D> location = GeometryTestUtils.defaultPoint();
        LocalDate date = LocalDate.of(2020, 9, 10);

        //when
        ResidentialComplex complex = ResidentialComplex.of(name, type, location, date);

        //then
        Assertions.assertThat(complex)
                .extracting("name", "type", "location", "approvalDate")
                .containsExactly(name, type, location, date);

    }

}