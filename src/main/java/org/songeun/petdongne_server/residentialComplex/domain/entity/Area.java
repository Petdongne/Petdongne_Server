package org.songeun.petdongne_server.residentialComplex.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;
import org.songeun.petdongne_server.global.exception.BusinessException;

import java.time.LocalDate;

import static org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus.AREA_MUST_BE_POSITIVE;
import static org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus.AREA_REQUIRED;

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
        ensureNotNull(areaInSquareMeters);
        ensureAreaInMeterIsPositive(areaInSquareMeters);

        return Area.builder()
                .areaInSquareMeters(areaInSquareMeters)
                .residentialComplex(residentialComplex).build();
    }

    public LocalDate getResidentialComplexApprovalDate(){
        return residentialComplex.approvalDate();
    }

    private static void ensureNotNull(Double areaInSquareMeters) {
        if (areaInSquareMeters == null){
            throw new BusinessException(AREA_REQUIRED);
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

}
