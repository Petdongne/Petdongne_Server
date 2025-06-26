package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.AddressType;
import org.songeun.petdongne_server.global.exception.BusinessException;

import static org.assertj.core.api.Assertions.*;
import static org.songeun.petdongne_server.addess.infrastructure.elasticsearch.error.AddressErrorStatus.ADDRESS_TYPE_NOT_FOUND;

class AddressTypeTest {

    @Test
    @DisplayName("주어진 이름과 매칭되는 AddressType을 반환한다.")
    void shouldReturnAddressTypeGivenValidName() {
        //given
        String name = "법정동";

        //when
        AddressType addressType = AddressType.fromName(name);

        //then
        assertThat(addressType).isEqualTo(AddressType.LEGAL_DONG_ADDRESS);
    }

    @Test
    @DisplayName("존재하지 않는 이름이면 예외를 던진다.")
    void shouldThrowExceptionGivenInvalidName(){
        //given
        String name = "법정똥";

        //when //then
        assertThatThrownBy(() -> AddressType.fromName(name))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ADDRESS_TYPE_NOT_FOUND.getMessage());
    }

}