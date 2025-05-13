package org.songeun.petdongne_server.residentialComplex.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;

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

        return Area.builder()
                .areaInSquareMeters(areaInSquareMeters)
                .residentialComplex(residentialComplex).build();
    }

    @Builder
    private Area(Double areaInSquareMeters, ResidentialComplex residentialComplex) {
        this.areaInSquareMeters = areaInSquareMeters;
        this.residentialComplex = residentialComplex;
    }

}
