package org.songeun.petdongne_server.facility.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.songeun.petdongne_server.facility.domain.error.FacilityErrorStatus;
import org.songeun.petdongne_server.global.common.BaseEntity;
import org.songeun.petdongne_server.global.converter.FacilityTypeConverter;
import org.songeun.petdongne_server.global.exception.BusinessException;

import static org.songeun.petdongne_server.facility.domain.error.FacilityErrorStatus.*;

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

    public static PetFacility of(Point<G2D> location, FacilityType type, String name){
        ensureNotNull(location, NotNullField.LOCATION);
        ensureNotNull(type, NotNullField.TYPE);
        ensureNotNull(name, NotNullField.NAME);

        ensureNameIsValid(name);

        return PetFacility.builder()
                .location(location)
                .type(type)
                .name(name).build();
    }


    private static <T> void ensureNotNull(T value, NotNullField notNullField) {
        if (value == null) {
            throw new BusinessException(notNullField.errorStatus);
        }
    }

    private static void ensureNameIsValid(String name) {
        if (name.trim().isEmpty()) {
            throw new BusinessException(FACILITY_NAME_REQUIRED);
        }
    }

    @Builder
    private PetFacility(Point<G2D> location, FacilityType type, String name) {
        this.location = location;
        this.type = type;
        this.name = name;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum NotNullField{
        LOCATION("경위도", FACILITY_LOCATION_REQUIRED),
        TYPE("유형", FACILITY_TYPE_REQUIRED),
        NAME("이름", FACILITY_NAME_REQUIRED),

        ;

        private final String description;
        private final FacilityErrorStatus errorStatus;
    }

}
