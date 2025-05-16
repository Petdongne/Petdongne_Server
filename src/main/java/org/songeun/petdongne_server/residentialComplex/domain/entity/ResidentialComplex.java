package org.songeun.petdongne_server.residentialComplex.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.songeun.petdongne_server.global.common.BaseEntity;
import org.songeun.petdongne_server.global.converter.ResidentialComplexTypeConverter;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus;

import java.time.LocalDate;

import static org.songeun.petdongne_server.residentialComplex.domain.error.ResidentialComplexErrorStatus.*;

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

    public LocalDate approvalDate() {
        return approvalDate;
    }

    public static ResidentialComplex of(String name, ResidentialComplexType type, Point<G2D> location, LocalDate approvalDate){
        ensureNotNull(name, NotNullField.NAME);
        ensureNotNull(type, NotNullField.TYPE);
        ensureNotNull(location, NotNullField.LOCATION);
        ensureNotNull(approvalDate, NotNullField.APPROVAL_DATE);

        ensureNameIsValid(name);
        ensureApprovalDateIsNotFuture(approvalDate);
      
        return ResidentialComplex.builder()
                .name(name)
                .type(type)
                .location(location)
                .approvalDate(approvalDate).build();
    }

    private static <T> void ensureNotNull(T value, NotNullField notNullField) {
        if (value == null) {
            throw new BusinessException(notNullField.errorStatus);
        }
    }

    private static void ensureNameIsValid(String name) {
        if (name.trim().isEmpty()) {
            throw new BusinessException(RESIDENTIAL_COMPLEX_NAME_REQUIRED);
        }
    }

    private static void ensureApprovalDateIsNotFuture(LocalDate approvalDate) {
        if (approvalDate.isAfter(LocalDate.now())) {
            throw new BusinessException(APPROVAL_DATE_IS_FUTURE);
        }
    }

    @Builder
    private ResidentialComplex(String name, ResidentialComplexType type, Point<G2D> location, LocalDate approvalDate) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.approvalDate = approvalDate;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum NotNullField {

        NAME("이름", RESIDENTIAL_COMPLEX_NAME_REQUIRED),
        TYPE("유형", RESIDENTIAL_COMPLEX_TYPE_REQUIRED),
        LOCATION("경위도", RESIDENTIAL_COMPLEX_LOCATION_REQUIRED),
        APPROVAL_DATE("사용 승인일", APPROVAL_DATE_IS_REQUIRED),
        ;

        private final String description;
        private final ResidentialComplexErrorStatus errorStatus;
    }

}
