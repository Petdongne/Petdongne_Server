package org.songeun.petdongne_server.domain.residentialComplex.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;
import org.songeun.petdongne_server.global.converter.TransactionTypeConverter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private Double price;

    @NotNull
    @Convert(converter = TransactionTypeConverter.class)
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Area area;

    @Builder
    private Transaction(LocalDate date, Double price, TransactionType type, Area area) {
        this.date = date;
        this.price = price;
        this.type = type;
        this.area = area;
    }

}
