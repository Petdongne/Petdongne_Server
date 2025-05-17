package org.songeun.petdongne_server.facility.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.songeun.petdongne_server.facility.domain.error.FacilityErrorStatus.*;
import static org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus.RESIDENTIAL_COMPLEX_NAME_REQUIRED;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.testSupport.GeometryTestUtils;

class PetFacilityTest {

    @Test
    @DisplayName("좌표, 시설 유형, 시설 이름으로 PetFacility 인스턴스를 생성한다.")
    void createPetFacility() {
        //given
        Point<G2D> location = GeometryTestUtils.defaultPoint();
        FacilityType type = FacilityType.PET_HOTEL;
        String name = "개편한 세상";

        //when
        PetFacility facility = PetFacility.of(location, type, name);

        //then
        assertThat(facility)
                .extracting("location", "type", "name")
                .containsExactly(location, type, name);
    }

    @Test
    @DisplayName("시설 위치(좌표)가 null이면 예외를 발생시킨다.")
    void fail_createPetFacility_whenLocationIsNull(){
        //given
        Point<G2D> invalidLocation = null;
        FacilityType type = FacilityType.PET_HOTEL;
        String name = "개편한 세상";


        //when //then
        assertThatThrownBy(() -> PetFacility.of(invalidLocation, type, name))
                .isInstanceOf(BusinessException.class)
                .hasMessage(FACILITY_LOCATION_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("시설 유형이 null이면 예외를 발생시킨다.")
    void fail_createPetFacility_whenTypeIsNull(){
        //given
        Point<G2D> location = GeometryTestUtils.defaultPoint();
        FacilityType invalidType = null;
        String name = "개편한 세상";


        //when //then
        assertThatThrownBy(() -> PetFacility.of(location, invalidType, name))
                .isInstanceOf(BusinessException.class)
                .hasMessage(FACILITY_TYPE_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("시설 이름이 null이면 예외를 발생시킨다.")
    void fail_createPetFacility_whenNameIsNull(){
        //given
        Point<G2D> location = GeometryTestUtils.defaultPoint();
        FacilityType type = FacilityType.PET_HOTEL;
        String invalidName = null;


        //when //then
        assertThatThrownBy(() -> PetFacility.of(location, type, invalidName))
                .isInstanceOf(BusinessException.class)
                .hasMessage(FACILITY_NAME_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("시설 이름이 공백이면 예외를 발생시킨다.")
    void fail_createPetFacility_whenNameIsInvalid(){
        //given
        Point<G2D> location = GeometryTestUtils.defaultPoint();;
        FacilityType type = FacilityType.PET_HOTEL;
        String invalidName = " ";


        //when //then
        assertThatThrownBy(() -> PetFacility.of(location, type, invalidName))
                .isInstanceOf(BusinessException.class)
                .hasMessage(FACILITY_NAME_REQUIRED.getMessage());
    }

}