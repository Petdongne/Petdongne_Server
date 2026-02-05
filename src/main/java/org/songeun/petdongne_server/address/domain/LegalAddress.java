package org.songeun.petdongne_server.address.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.songeun.petdongne_server.global.common.BaseEntity;

// todo gin index, extension check
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class LegalAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String fullAddress; // full name

    @NotNull
    private String addressInitials; // init names

    @Embedded
    private LegalAddressParts addressParts;

    @NotNull
    @Column(columnDefinition = "geometry(MultiPolygon, 4326)")
    private MultiPolygon<G2D> polygon;

    @NotNull
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point<G2D> centerPoint;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RegionAddressLevel regionAddressLevel;

    @NotNull
    @Column(length = 6)
    private String geohash;

}
