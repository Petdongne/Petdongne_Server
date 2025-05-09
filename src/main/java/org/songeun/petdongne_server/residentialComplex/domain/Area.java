package org.songeun.petdongne_server.residentialComplex.domain;

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

    @Builder
    private Area(Double areaInSquareMeters, ResidentialComplex residentialComplex) {
        this.areaInSquareMeters = areaInSquareMeters;
        this.residentialComplex = residentialComplex;
    }

}
