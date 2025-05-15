package org.songeun.petdongne_server.residentialComplex.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResidentialComplexTypeTest {

    @Test
    @DisplayName("올바른 코드 값이면 해당 시설 유형을 반환한다.")
    void validCode() {
        //given
        String validCode = "APARTMENT";

        //when
        ResidentialComplexType complexType = ResidentialComplexType.fromCode(validCode);

        //then
        assertThat(complexType).isEqualTo(ResidentialComplexType.APARTMENT);
    }

    @Test
    @DisplayName("유효하지 않은 코드 값일 경우 예외를 발생시킨다.")
    public void invalidCode(){
        //given
        String invalidCode = "CAT_TOWER";

        //when //then
        assertThatThrownBy(()->ResidentialComplexType.fromCode(invalidCode))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ResidentialComplexErrorStatus.RESIDENTIAL_COMPLEX_TYPE_NOT_FOUND.getMessage());
    }

}