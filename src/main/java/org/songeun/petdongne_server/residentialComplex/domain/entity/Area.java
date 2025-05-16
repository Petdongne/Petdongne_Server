package org.songeun.petdongne_server.residentialComplex.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus;

import java.time.LocalDate;

import static org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Area extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private Double areaInSquareMeters;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residential_complex_id")
    private ResidentialComplex residentialComplex;

    public static Area of(Double areaInSquareMeters, ResidentialComplex residentialComplex){
        ensureNotNull(areaInSquareMeters, NotNullField.AREA_IN_SQUARE_METERS);
        ensureNotNull(residentialComplex, NotNullField.RESIDENTIAL_COMPLEX);

        ensureAreaInMeterIsPositive(areaInSquareMeters);

        return Area.builder()
                .areaInSquareMeters(areaInSquareMeters)
                .residentialComplex(residentialComplex).build();
    }

    public LocalDate getResidentialComplexApprovalDate(){
        return residentialComplex.approvalDate();
    }

    private static <T> void ensureNotNull(T value, NotNullField notNullField) {
        if (value == null) {
            throw new BusinessException(notNullField.errorStatus);
        }
    }

    private static void ensureAreaInMeterIsPositive(Double areaInSquareMeters) {
        if (areaInSquareMeters <= 0){
            throw new BusinessException(AREA_MUST_BE_POSITIVE);
        }
    }

    @Builder
    private Area(Double areaInSquareMeters, ResidentialComplex residentialComplex) {
        this.areaInSquareMeters = areaInSquareMeters;
        this.residentialComplex = residentialComplex;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum NotNullField{
        AREA_IN_SQUARE_METERS("주거 면적 value (단위: 제곱 미터)", AREA_REQUIRED),
        RESIDENTIAL_COMPLEX("주거 단지", RESIDENTIAL_COMPLEX_REQUIRED),

        ;

        private final String description;
        private final ResidentialComplexErrorStatus errorStatus;
    }

}
