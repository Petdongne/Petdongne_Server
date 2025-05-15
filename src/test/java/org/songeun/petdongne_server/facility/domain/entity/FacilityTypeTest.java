package org.songeun.petdongne_server.facility.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.facility.domain.error.FacilityErrorStatus;
import org.songeun.petdongne_server.global.exception.BusinessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FacilityTypeTest {

    @Test
    @DisplayName("올바른 코드 값이면 해당 시설 유형을 반환한다.")
    void validCode() {
        //given
        String validCode = "VET";

        //when
        FacilityType facilityType = FacilityType.fromCode(validCode);

        //then
        assertThat(facilityType).isEqualTo(FacilityType.VET);
    }

    @Test
    @DisplayName("유효하지 않은 코드 값일 경우 예외를 발생시킨다.")
    public void invalidCode(){
        //given
        String invalidCode = "WAFFLE_SHOP";

        //when //then
        assertThatThrownBy(()->FacilityType.fromCode(invalidCode))
                .isInstanceOf(BusinessException.class)
                .hasMessage(FacilityErrorStatus.FACILITY_TYPE_NOT_FOUND.getMessage());
    }

}