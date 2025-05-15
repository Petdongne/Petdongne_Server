package org.songeun.petdongne_server.residentialComplex.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;
import org.songeun.petdongne_server.global.converter.TransactionTypeConverter;
import org.songeun.petdongne_server.global.exception.BusinessException;

import java.time.LocalDate;
import java.util.Objects;

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
        ensureNotNull(type);
        ensureNotNull(area);
        ensureDateIsBeforeOrEqualNow(date);
        ensureDateIsEqualOrAfterApproveDate(date, area);
        ensurePriceIsPositive(price);

        return Transaction.builder()
                .date(date)
                .price(price)
                .type(type)
                .area(area).build();
    }

    private static void ensureNotNull(TransactionType type) {
        if(type == null){
            throw new BusinessException(TRANSACTION_TYPE_REQUIRED);
        }
    }

    private static void ensureNotNull(Area area) {
        if(area == null){
            throw new BusinessException(AREA_REQUIRED);
        }
    }

    private static void ensureDateIsBeforeOrEqualNow(LocalDate date) {
        if(date.isAfter(LocalDate.now())) {
            throw new BusinessException(TRANSACTION_DATE_IS_AFTER_TODAY);
        }
    }

    private static void ensureDateIsEqualOrAfterApproveDate(LocalDate date, Area area) {
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

}
