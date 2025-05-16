package org.songeun.petdongne_server.residentialComplex.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;
import org.songeun.petdongne_server.global.converter.TransactionTypeConverter;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus;

import java.time.LocalDate;

import static org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long price;

    @NotNull
    @Convert(converter = TransactionTypeConverter.class)
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Area area;

    public static Transaction of(LocalDate date, Long price, TransactionType type, Area area){
        ensureNotNull(date, NotNullField.DATE);
        ensureNotNull(price, NotNullField.PRICE);
        ensureNotNull(type, NotNullField.TYPE);
        ensureNotNull(area, NotNullField.AREA);

        ensureTxnDateIsNotFuture(date);
        ensureTxnDateIsNotEarlierThanApproveDate(date, area);
        ensurePriceIsPositive(price);

        return Transaction.builder()
                .date(date)
                .price(price)
                .type(type)
                .area(area).build();
    }

    private static <T> void ensureNotNull(T value, NotNullField notNullField) {
        if (value == null) {
            throw new BusinessException(notNullField.errorStatus);
        }
    }

    private static void ensureTxnDateIsNotFuture(LocalDate date) {
        if(date.isAfter(LocalDate.now())) {
            throw new BusinessException(TRANSACTION_DATE_IS_FUTURE);
        }
    }

    private static void ensureTxnDateIsNotEarlierThanApproveDate(LocalDate date, Area area) {
        if (date.isBefore(area.getResidentialComplexApprovalDate())) {
            throw new BusinessException(TRANSACTION_DATE_BEFORE_APPROVE_DATE);
        }
    }

    private static void ensurePriceIsPositive(Long price) {
        if(price <= 0){
            throw new BusinessException(PRICE_MUST_BE_POSITIVE);
        }
    }

    @Builder
    private Transaction(LocalDate date, Long price, TransactionType type, Area area) {
        this.date = date;
        this.price = price;
        this.type = type;
        this.area = area;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum NotNullField{
        DATE("거래일자", TRANSACTION_DATE_REQUIRED),
        PRICE("거래 금액", TRANSACTION_PRICE_REQUIRED),
        TYPE("거래 유형", TRANSACTION_TYPE_REQUIRED),
        AREA("주거 면적", AREA_REQUIRED),

        ;

        private final String description;
        private final ResidentialComplexErrorStatus errorStatus;
    }

}
