package org.songeun.petdongne_server.facility.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.songeun.petdongne_server.global.common.BaseEntity;
import org.songeun.petdongne_server.global.converter.FacilityTypeConverter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetFacility extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Convert(converter = FacilityTypeConverter.class)
    private FacilityType type;

    @NotNull
    private Point<G2D> location;

    @Builder
    private PetFacility(Point<G2D> location, FacilityType type, String name) {
        this.location = location;
        this.type = type;
        this.name = name;
    }

}
