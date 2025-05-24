package org.songeun.petdongne_server.residentialComplex.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.testSupport.GeometryTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus.*;

class TransactionTest {

    @Test
    @DisplayName("거래 날짜, 금액, 유형, 면적으로 Transaction 인스턴스를 생성한다.")
    void createTransaction(){
        //given
        LocalDate txnDate = LocalDate.now();
        Long price = 100_000L;
        TransactionType type = TransactionType.SALE;
        Double areaInSquare = 164.49;

        LocalDate approvalDate = LocalDate.of(2020, 9, 10);
        Area area = createArea(areaInSquare, createResidentialComplex(approvalDate));

        //when
        Transaction transaction = Transaction.of(txnDate, price, type, area);

        //then
        assertThat(transaction).extracting("date", "price", "type", "area")
                .containsExactly(txnDate, price, type, area);
    }

    @Test
    @DisplayName("거래 일자가 null이면 예외를 발생시킨다.")
    void fail_createTransaction_whenTxnDateIsNull(){
        //given
        LocalDate inValidTxnDate = null;
        Long price = 100_000L;
        TransactionType type = TransactionType.SALE;
        Double areaInSquare = 164.49;

        LocalDate approvalDate = LocalDate.of(2020, 9, 10);
        Area area = createArea(areaInSquare, createResidentialComplex(approvalDate));

        //when //then
        assertThatThrownBy(() -> Transaction.of(inValidTxnDate, price, type, area))
                .isInstanceOf(BusinessException.class)
                .hasMessage(TRANSACTION_DATE_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("거래 금액이 null이면 예외를 발생시킨다.")
    void fail_createTransaction_whenPriceIsNull(){
        //given
        LocalDate txnDate = LocalDate.now();
        Long invalidPrice = null;
        TransactionType type = TransactionType.SALE;
        Double areaInSquare = 164.49;

        LocalDate approvalDate = LocalDate.of(2020, 9, 10);
        Area area = createArea(areaInSquare, createResidentialComplex(approvalDate));

        //when //then
        assertThatThrownBy(() -> Transaction.of(txnDate, invalidPrice, type, area))
                .isInstanceOf(BusinessException.class)
                .hasMessage(TRANSACTION_PRICE_REQUIRED.getMessage());
    }


    @Test
    @DisplayName("거래 유형이 null이면 예외를 발생시킨다.")
    void fail_createTransaction_whenTypeIsNull(){
        //given
        LocalDate txnDate = LocalDate.now();
        Long price = 100_000L;
        TransactionType invalidType = null;
        Double areaInSquare = 164.49;

        LocalDate approvalDate = LocalDate.of(2020, 9, 10);
        Area area = createArea(areaInSquare, createResidentialComplex(approvalDate));

        //when //then
        assertThatThrownBy(() -> Transaction.of(txnDate, price, invalidType, area))
                .isInstanceOf(BusinessException.class)
                .hasMessage(TRANSACTION_TYPE_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("주거 면적이 null이면 예외를 발생시킨다.")
    void fail_createTransaction_whenAreaIsNull(){
        //given
        LocalDate txnDate = LocalDate.now();
        Long price = 100_000L;
        TransactionType type = TransactionType.SALE;
        Area invalidArea = null;

        //when //then
        assertThatThrownBy(() -> Transaction.of(txnDate, price, type, invalidArea))
                .isInstanceOf(BusinessException.class)
                .hasMessage(AREA_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("거래 금액이 0원이면 예외를 발생시킨다.")
    void fail_createTransaction_whenPriceIsZero(){
        //given
        LocalDate txnDate = LocalDate.now();
        Long invalidPrice = 0L;
        TransactionType type = TransactionType.SALE;
        Double areaInSquare = 164.49;

        LocalDate approvalDate = LocalDate.of(2020, 9, 10);
        Area area = createArea(areaInSquare, createResidentialComplex(approvalDate));

        //when //then
        assertThatThrownBy(() -> Transaction.of(txnDate, invalidPrice, type, area))
                .isInstanceOf(BusinessException.class)
                .hasMessage(TRANSACTION_PRICE_MUST_BE_POSITIVE.getMessage());
    }

    @Test
    @DisplayName("거래 금액이 0원 미만이면 예외를 발생시킨다.")
    void fail_createTransaction_whenPriceIsNegative(){
        //given
        LocalDate txnDate = LocalDate.now();
        Long invalidPrice = -10L;
        TransactionType type = TransactionType.SALE;
        Double areaInSquare = 164.49;

        LocalDate approvalDate = LocalDate.of(2020, 9, 10);
        Area area = createArea(areaInSquare, createResidentialComplex(approvalDate));

        //when //then
        assertThatThrownBy(() -> Transaction.of(txnDate, invalidPrice, type, area))
                .isInstanceOf(BusinessException.class)
                .hasMessage(TRANSACTION_PRICE_MUST_BE_POSITIVE.getMessage());
    }

    @Test
    @DisplayName("거래 일자가 미래이면 예외를 발생시킨다.")
    void fail_createTransaction_whenTxnDateIsFuture(){
        //given
        LocalDate inValidTxnDate = LocalDate.of(2300, 9, 10);
        Long price = 100_000L;
        TransactionType type = TransactionType.SALE;
        Double areaInSquare = 164.49;

        LocalDate approvalDate = LocalDate.of(2020, 9, 10);
        Area area = createArea(areaInSquare, createResidentialComplex(approvalDate));

        //when //then
        assertThatThrownBy(() -> Transaction.of(inValidTxnDate, price, type, area))
                .isInstanceOf(BusinessException.class)
                .hasMessage(TRANSACTION_DATE_IS_FUTURE.getMessage());

    }

    @Test
    @DisplayName("거래 일자가 사용 승인일보다 이전이라면 예외를 발생시킨다.")
    void fail_createTransaction_whenTxnDateIsEarlierThanApproveDate(){
        //given
        LocalDate inValidTxnDate = LocalDate.of(2000, 9, 10);
        Long price = 100_000L;
        TransactionType type = TransactionType.SALE;
        Double areaInSquare = 164.49;

        LocalDate approvalDate = LocalDate.of(2020, 9, 10);
        Area area = createArea(areaInSquare, createResidentialComplex(approvalDate));

        //when //then
        assertThatThrownBy(() -> Transaction.of(inValidTxnDate, price, type, area))
                .isInstanceOf(BusinessException.class)
                .hasMessage(TRANSACTION_DATE_BEFORE_APPROVAL_DATE.getMessage());

    }

    private Area createArea(Double areaInSquareMeters, ResidentialComplex complex){
        return Area.of(areaInSquareMeters, complex);
    }

    private ResidentialComplex createResidentialComplex(LocalDate approvalDate) {
        return ResidentialComplex.of(
                "왈왈아파트",
                ResidentialComplexType.APARTMENT,
                GeometryTestUtils.defaultPoint(),
                approvalDate
        );
    }
  
}