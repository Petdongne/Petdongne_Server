package org.songeun.petdongne_server.facility.domain.entity;

import static org.assertj.core.api.Assertions.*;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

}