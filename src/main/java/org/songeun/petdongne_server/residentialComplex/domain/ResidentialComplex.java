package org.songeun.petdongne_server.residentialComplex.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.songeun.petdongne_server.global.common.BaseEntity;
import org.songeun.petdongne_server.global.converter.ResidentialComplexTypeConverter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResidentialComplex extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Convert(converter = ResidentialComplexTypeConverter.class)
    private ResidentialComplexType type;

    @NotNull
    private Point<G2D> location;

    @NotNull
    private LocalDate approvalDate;

    @Builder
    public ResidentialComplex(String name, ResidentialComplexType type, Point<G2D> location, LocalDate approvalDate) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.approvalDate = approvalDate;
    }

}
