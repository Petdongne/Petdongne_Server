package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.AddressHierarchy;
import org.songeun.petdongne_server.global.exception.BusinessException;

import static org.assertj.core.api.Assertions.*;
import static org.songeun.petdongne_server.addess.infrastructure.elasticsearch.error.AddressErrorStatus.ADDRESS_HIERARCHY_INVALID_FORMAT;
import static org.songeun.petdongne_server.addess.infrastructure.elasticsearch.error.AddressErrorStatus.ADDRESS_HIERARCHY_NOT_FOUND;

class AddressHierarchyTest {

    @Test
    @DisplayName("String type의 level값을 통해 매칭되는 AddressHierarchy를 반환한다.")
    void shouldReturnAddressHierarchyFromStringLevel() {
        //given
        String level = "1";

        //when
        AddressHierarchy hierarchy = AddressHierarchy.fromLevel(level);

        //then
        assertThat(hierarchy).isEqualTo(AddressHierarchy.SIDO);
        assertThat(hierarchy.getLevel()).isEqualTo(1);
    }

    @Test
    @DisplayName("숫자가 아닌 level 값이면 예외를 던진다.")
    void shouldThrowExceptionIfLevelIsNotNumeric() {
        //given
        String level = "일";

        //when //then
        assertThatThrownBy(() -> AddressHierarchy.fromLevel(level))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ADDRESS_HIERARCHY_INVALID_FORMAT.getMessage());
    }

    @Test
    @DisplayName("Integer type의 level값을 통해 매칭되는 AddressHierarchy를 반환한다.")
    void shouldReturnAddressHierarchyFromIntegerLevel() {
        //given
        Integer level = 1;

        //when
        AddressHierarchy hierarchy = AddressHierarchy.fromLevel(level);

        //then
        assertThat(hierarchy).isEqualTo(AddressHierarchy.SIDO);
        assertThat(hierarchy.getLevel()).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 level이면 예외를 던진다.")
    void shouldThrowExceptionForUnknownLevel() {
        //given
        Integer level = 5;

        //when //then
        assertThatThrownBy(() -> AddressHierarchy.fromLevel(level))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ADDRESS_HIERARCHY_NOT_FOUND.getMessage());
    }

}