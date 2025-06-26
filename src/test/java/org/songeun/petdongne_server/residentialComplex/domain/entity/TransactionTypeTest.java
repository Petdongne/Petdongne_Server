package org.songeun.petdongne_server.residentialComplex.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.global.exception.BusinessException;

import static org.assertj.core.api.Assertions.*;
import static org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus.TRANSACTION_TYPE_NOT_FOUND;


class TransactionTypeTest {

    @Test
    @DisplayName("코드와 매칭되는 TransactionType을 반환한다.")
    void shouldReturnTransactionTypeGivenValidCode() {
        //given
        String code = "SALE";

        //when
        TransactionType type = TransactionType.fromCode(code);

        //then
        assertThat(type).isEqualTo(TransactionType.SALE);
    }

    @Test
    @DisplayName("존재하지 않는 코드이면 예외를 던진다.")
    void shouldThrowExceptionGivenInvalidCode() {
        //given
        String code = "SALEE";

        //when //then
        assertThatThrownBy(() -> TransactionType.fromCode(code))
                .isInstanceOf(BusinessException.class)
                .hasMessage(TRANSACTION_TYPE_NOT_FOUND.getMessage());
    }

}